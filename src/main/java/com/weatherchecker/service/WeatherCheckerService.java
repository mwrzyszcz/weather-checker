package com.weatherchecker.service;

import static com.weatherchecker.service.JsonMemberNameValues.ADMINISTRATIVE_AREA;
import static com.weatherchecker.service.JsonMemberNameValues.DAILY_FORECASTS;
import static com.weatherchecker.service.JsonMemberNameValues.LOCALIZED_NAME;
import static com.weatherchecker.service.JsonMemberNameValues.LOCATION_KEY;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weatherchecker.dto.ForecastInformationDto;
import com.weatherchecker.dto.ForecastResponseDto;
import com.weatherchecker.dto.LocationDto;
import com.weatherchecker.exception.PostalCodeIllegalFormatException;
import com.weatherchecker.exception.PostalCodeNotFoundException;
import com.weatherchecker.mapper.ForecastMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherCheckerService {

  Logger logger = LoggerFactory.getLogger(WeatherCheckerService.class);

  private static final String PL_POSTAL_CODE_REGEX = "\\d{2}-\\d{3}";
  private static final Pattern PATTERN = Pattern.compile(PL_POSTAL_CODE_REGEX);
  private final RestTemplate restTemplate;

  @Value("${url.5.day.forecast}")
  private String url5dayForecast;

  @Value("${url.location}")
  private String urlLocation;

  @Value("${api.key}")
  private String apiKey;

  public WeatherCheckerService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ForecastResponseDto process(final String postalCode) {

    validatePostalCode(postalCode);

    logger.info("Request for postal code: {}", postalCode);
    final var locationInfoJson =
        restTemplate.getForObject(buildLocationUri(postalCode), String.class);
    final var locationDto = extractLocationInfo(locationInfoJson);

    logger.info("Request for 5 day forecast with location key: {}", locationDto.getLocationKey());
    final var forecastInfoJson =
        restTemplate.getForObject(buildForecastUri(locationDto), String.class);

    logger.info("Forecast fetched successfully");
    return new ForecastResponseDto(
        postalCode, locationDto.getVoivodeship(), extractForecastInformationDtos(forecastInfoJson));
  }

  private void validatePostalCode(String postalCode) {
    if (!PATTERN.matcher(postalCode).matches() || postalCode.isBlank()) {
      logger.error("Postal code {} has wrong format", postalCode);
      throw new PostalCodeIllegalFormatException("Wrong format of postal code!");
    }
  }

  private URI buildLocationUri(String postalCode) {
    return UriComponentsBuilder.fromUriString(urlLocation)
        .queryParam("apikey", apiKey)
        .queryParam("q", postalCode)
        .queryParam("language", "pl-pl")
        .queryParam("details", false)
        .build()
        .toUri();
  }

  private URI buildForecastUri(LocationDto locationDto) {
    return UriComponentsBuilder.fromUriString(url5dayForecast)
        .queryParam("apikey", apiKey)
        .queryParam("language", "pl-pl")
        .queryParam("metric", true)
        .queryParam("details", false)
        .buildAndExpand(Map.of("locationKey", locationDto.getLocationKey()))
        .toUri();
  }

  private LocationDto extractLocationInfo(String json) {
    LocationDto locationDto = new LocationDto();
    Gson gson = new Gson();
    JsonArray asJsonArray = JsonParser.parseString(json).getAsJsonArray();

    Stream.of(asJsonArray.iterator())
        .filter(Iterator::hasNext)
        .map(element -> gson.fromJson(element.next(), JsonObject.class))
        .findFirst()
        .ifPresentOrElse(
            jsonObject -> {
              locationDto.setLocationKey(jsonObject.get(LOCATION_KEY).getAsString());
              locationDto.setVoivodeship(
                  jsonObject
                      .get(ADMINISTRATIVE_AREA)
                      .getAsJsonObject()
                      .get(LOCALIZED_NAME)
                      .getAsString());
            },
            () -> new PostalCodeNotFoundException("Postal code does not exist"));
    return locationDto;
  }

  private List<ForecastInformationDto> extractForecastInformationDtos(String json) {
    JsonObject asJsonObject = JsonParser.parseString(json).getAsJsonObject();
    JsonArray dailyForecasts = asJsonObject.get(DAILY_FORECASTS).getAsJsonArray();
    Gson gson = new Gson();

    final var iterator = dailyForecasts.iterator();
    List<ForecastInformationDto> forecastInformations = new ArrayList<>();
    while (iterator.hasNext()) {
      final var jsonObject = gson.fromJson(iterator.next(), JsonObject.class);
      forecastInformations.add(ForecastMapper.from(jsonObject));
    }
    return forecastInformations;

    /** interesting case why iterator doesn't work in functional way (only 1 loop) */
    /*    return Stream.of(dailyForecasts.iterator())
    .takeWhile(Iterator::hasNext)
    .map(jsonElement -> gson.fromJson(jsonElement.next(), JsonObject.class))
    .map(ForecastMapper::from)
    .collect(Collectors.toList());*/
  }
}
