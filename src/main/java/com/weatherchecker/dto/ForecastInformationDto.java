package com.weatherchecker.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ForecastInformationDto {

  private final LocalDate date;
  private final DayOfWeek dayOfWeek;
  private final Double minTemp;
  private final Double maxTemp;

  public ForecastInformationDto(
      LocalDate date, DayOfWeek dayOfWeek, Double minTemp, Double maxTemp) {
    this.date = date;
    this.dayOfWeek = dayOfWeek;
    this.maxTemp = maxTemp;
    this.minTemp = minTemp;
  }

  public LocalDate getDate() {
    return date;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public Double getMinTemp() {
    return minTemp;
  }

  public Double getMaxTemp() {
    return maxTemp;
  }
}
