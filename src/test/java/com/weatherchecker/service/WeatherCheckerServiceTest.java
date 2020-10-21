package com.weatherchecker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.weatherchecker.dto.ForecastInformationDto;
import com.weatherchecker.dto.ForecastResponseDto;
import com.weatherchecker.exception.PostalCodeIllegalFormatException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class WeatherCheckerServiceTest {

  @Mock RestTemplate restTemplate;
  @InjectMocks private WeatherCheckerService weatherCheckerService;

  private static String correctLocationJson;
  private static String correctForecastJson;


  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(weatherCheckerService, "apiKey", "apiKeyValue");
    ReflectionTestUtils.setField(weatherCheckerService, "urlLocation", "locationUrl");
    ReflectionTestUtils.setField(weatherCheckerService, "url5dayForecast", "forecastUrl");
  }

  @BeforeAll
  static void setUp() throws IOException, URISyntaxException {
    correctLocationJson = readFileFromResources("correct_location_info_json.json");
    correctForecastJson = readFileFromResources("correct_5_daily_forecast_json.json");

  }

  @Test
  public void shouldThrowsPostalCodeIllegalFormatExceptionIfPostalCodeIsWrong() {
    // given
    final var expected = "Wrong format of postal code!";

    // when
    PostalCodeIllegalFormatException postalCodeIllegalFormatException =
        Assertions.assertThrows(
            PostalCodeIllegalFormatException.class, () -> weatherCheckerService.process("12345"));

    // then
    Assertions.assertEquals(expected, postalCodeIllegalFormatException.getMessage());
  }

  @Disabled
  @Test
  public void shouldReturnCorrectResponseObjectIfPostalCodeCorrect() {
    // given
    String postalCode = "20-400";
    Mockito.when(restTemplate.getForObject(any(URI.class), eq(String.class)))
        .thenReturn(correctLocationJson);

    Mockito.when(restTemplate.getForObject(any(URI.class), eq(String.class)))
        .thenReturn(correctForecastJson);

    ForecastResponseDto expectedForecastResponseDto =
        new ForecastResponseDto(postalCode, "Lubelskie", CORRECT_FORECAST_INFORMATION_DTOS);

    // when
    final var result = weatherCheckerService.process(postalCode);

    // then
    Assertions.assertEquals(expectedForecastResponseDto, result);
  }

  private static String readFileFromResources(String filename)
      throws URISyntaxException, IOException {
    URL resource = WeatherCheckerServiceTest.class.getClassLoader().getResource(filename);
    byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
    return new String(bytes);
  }

  private static final List<ForecastInformationDto> CORRECT_FORECAST_INFORMATION_DTOS =
      List.of(
          new ForecastInformationDto(LocalDate.of(2020, 10, 19), DayOfWeek.MONDAY, 3.2, 10.0),
          new ForecastInformationDto(LocalDate.of(2020, 10, 20), DayOfWeek.TUESDAY, 6.6, 11.2),
          new ForecastInformationDto(LocalDate.of(2020, 10, 21), DayOfWeek.WEDNESDAY, 7.8, 13.7),
          new ForecastInformationDto(LocalDate.of(2020, 10, 22), DayOfWeek.THURSDAY, 10.5, 17.5),
          new ForecastInformationDto(LocalDate.of(2020, 10, 23), DayOfWeek.FRIDAY, 10.2, 15.6));
}
