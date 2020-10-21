package com.weatherchecker.dto;

import java.util.List;

public class ForecastResponseDto {

  private final String postalCode;
  private final String voivodeship;
  private final List<ForecastInformationDto> forecastInformationDtos;

  public ForecastResponseDto(
      String postalCode, String voivodeship, List<ForecastInformationDto> forecastInformationDtos) {
    this.postalCode = postalCode;
    this.voivodeship = voivodeship;
    this.forecastInformationDtos = forecastInformationDtos;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getVoivodeship() {
    return voivodeship;
  }

  public List<ForecastInformationDto> getForecastInformationDtos() {
    return forecastInformationDtos;
  }
}
