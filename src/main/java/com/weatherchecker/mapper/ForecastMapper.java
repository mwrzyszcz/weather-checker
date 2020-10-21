package com.weatherchecker.mapper;

import com.google.gson.JsonObject;
import com.weatherchecker.dto.ForecastInformationDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ForecastMapper {

  private static final String TEMPERATURE = "Temperature";
  private static final String MINIMUM = "Minimum";
  private static final String VALUE = "Value";

  public static ForecastInformationDto from(JsonObject jsonObject) {
    return new ForecastInformationDto(
        obtainLocalDate(jsonObject),
        DayOfWeek.from(obtainLocalDate(jsonObject)),
        jsonObject
            .get(TEMPERATURE)
            .getAsJsonObject()
            .get(MINIMUM)
            .getAsJsonObject()
            .get(VALUE)
            .getAsDouble(),
        jsonObject
            .get(TEMPERATURE)
            .getAsJsonObject()
            .get("Maximum")
            .getAsJsonObject()
            .get(VALUE)
            .getAsDouble());
  }

  private static LocalDate obtainLocalDate(JsonObject jsonObject) {
    return LocalDateTime.parse(
            jsonObject.get("Date").getAsString(), DateTimeFormatter.ISO_DATE_TIME)
        .toLocalDate();
  }
}
