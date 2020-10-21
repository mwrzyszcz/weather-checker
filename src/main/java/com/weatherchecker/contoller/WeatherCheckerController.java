package com.weatherchecker.contoller;

import com.weatherchecker.dto.ForecastResponseDto;
import com.weatherchecker.service.WeatherCheckerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/forecast")
public class WeatherCheckerController {

  private final WeatherCheckerService weatherCheckerService;

  public WeatherCheckerController(WeatherCheckerService weatherCheckerService) {
    this.weatherCheckerService = weatherCheckerService;
  }

  @GetMapping(path = "/daily/5day/{postalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ForecastResponseDto> getDailyForecast(@PathVariable String postalCode) {
    return ResponseEntity.ok(weatherCheckerService.process(postalCode));
  }
}
