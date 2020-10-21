package com.weatherchecker.dto;

public class LocationDto {

  private String locationKey;
  private String voivodeship;

  public String getLocationKey() {
    return locationKey;
  }

  public void setLocationKey(String locationKey) {
    this.locationKey = locationKey;
  }

  public String getVoivodeship() {
    return voivodeship;
  }

  public void setVoivodeship(String voivodeship) {
    this.voivodeship = voivodeship;
  }
}
