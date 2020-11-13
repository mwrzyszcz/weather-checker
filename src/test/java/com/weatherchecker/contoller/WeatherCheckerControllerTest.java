package com.weatherchecker.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.weatherchecker.dto.ForecastInformationDto;
import com.weatherchecker.service.WeatherCheckerService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class WeatherCheckerControllerTest {

  private static String correctLocationJson;
  private static String correctForecastJson;
  private static String correctResponseJson;

  @Autowired private MockMvc mockMvc;
  @MockBean private WeatherCheckerService weatherCheckerService;
  @MockBean private RestTemplate restTemplate;

  @BeforeAll
  static void setUp() throws IOException, URISyntaxException {
    correctLocationJson = readFileFromResources("correct_location_info_json.json");
    correctForecastJson = readFileFromResources("correct_5_daily_forecast_json.json");
    correctResponseJson = readFileFromResources("correct_response_json.json");
  }

  @Test
  public void shouldReturnCorrectResponse() throws Exception {
    var correctPostalCode = "20-400";

    Mockito.when(restTemplate.getForObject(any(), eq(String.class)))
        .thenReturn(correctLocationJson);

    Mockito.when(restTemplate.getForObject(any(), eq(String.class)))
        .thenReturn(correctForecastJson);

    // when
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/forecast/daily/5day/" + correctPostalCode))

        // then
        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(MockMvcResultMatchers.content().json(correctResponseJson.to))
        .andExpect(MockMvcResultMatchers.handler().handlerType(WeatherCheckerController.class))
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void shouldReturn404IfParameterIsEmpty() throws Exception {
    final var wrongPostalCode = "1231200";
    // when
    //    Mockito.when(weatherCheckerService.process());
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/forecast/daily/5day/" + wrongPostalCode))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void shouldReturn500BadRequestIfWrongPostalCode() throws Exception {
    // given
    final var wrongPostalCode = "1231200";
    final var exceptionMessage = "Wrong format of postal code!";

    // when
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/forecast/daily/5day/" + wrongPostalCode))

        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.handler().handlerType(WeatherCheckerController.class))
        .andExpect(
            MockMvcResultMatchers.content()
                .contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
        .andExpect(MockMvcResultMatchers.content().string(exceptionMessage));
  }

  private static String readFileFromResources(String filename)
      throws URISyntaxException, IOException {
    URL resource = WeatherCheckerControllerTest.class.getClassLoader().getResource(filename);
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
