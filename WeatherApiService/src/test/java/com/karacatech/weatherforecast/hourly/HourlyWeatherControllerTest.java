package com.karacatech.weatherforecast.hourly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HourlyWeatherController.class)
public class HourlyWeatherControllerTest {

    private static final String END_POINT = "/api/v1/hourly";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HourlyWeatherService hourlyWeatherService;

    @MockBean
    GeolocationService geolocationService;

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
        mockMvc.perform(get(END_POINT))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByIPAddressShouldReturn400BadRequestBecauseGeolocationException() throws Exception, GeolocationException {
        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT).header("X-Current-Hour", "0"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByIPAddressShouldReturn204NoContent() throws GeolocationException, Exception {
        int currentHour = 9;
        Location location = new Location().code("IST_TR");

        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenReturn(location);
        Mockito.when(hourlyWeatherService.getByLocation(location, currentHour))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT).header("X-Current-Hour", currentHour))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn200OK() throws Exception, GeolocationException {
        int currentHour = 8;
        Location location = new Location();
        location.code("IST_TR");
        location.setRegionName("Istanbul");
        location.setCityName("Istanbul");
        location.setCountryName("Turkey");
        location.setCountryCode("TR");

        HourlyWeather forecast1 = new HourlyWeather()
                .location(location)
                .hourOfDay(12)
                .temperature(13)
                .precipitation(0)
                .status("Cloudy");

        HourlyWeather forecast2 = new HourlyWeather()
                .location(location)
                .hourOfDay(14)
                .temperature(14)
                .precipitation(0)
                .status("Cloudy");

        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenReturn(location);
        Mockito.when(hourlyWeatherService.getByLocation(location, currentHour))
                .thenReturn(List.of(forecast1, forecast2));

        String expectedLocation = location.toString();

        mockMvc.perform(get(END_POINT).header("X-Current-Hour", String.valueOf(currentHour)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(12)))
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn400BadRequest() throws Exception {

        String locationCode = "IST_TR";
        String requestUrl = END_POINT + "/" + locationCode;

        mockMvc.perform(get(requestUrl))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception {
        int currentHour = 9;
        String locationCode = "IST_TR";
        String requestUrl = END_POINT + "/" + locationCode;

        Mockito.when(hourlyWeatherService.getByLocationCode(locationCode, currentHour))
                .thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(requestUrl).header("X-Current-Hour", String.valueOf(currentHour)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn204NoContent() throws Exception {
        int currentHour = 8;
        String locationCode = "IST_TR";
        String requestUrl = END_POINT + "/" + locationCode;

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("Istanbul");
        location.setRegionName("Istanbul");
        location.setCountryName("Turkey");
        location.setCountryCode("TR");

        HourlyWeather forecast1 = new HourlyWeather()
                .location(location)
                .hourOfDay(12)
                .temperature(13)
                .precipitation(0)
                .status("Cloudy");

        HourlyWeather forecast2 = new HourlyWeather()
                .location(location)
                .hourOfDay(14)
                .temperature(14)
                .precipitation(0)
                .status("Cloudy");

        var hourlyForecast = List.of(forecast1, forecast2);

        Mockito.when(hourlyWeatherService.getByLocationCode(locationCode, currentHour))
                .thenReturn(hourlyForecast);

        mockMvc.perform(get(requestUrl).header("X-Current-Hour", String.valueOf(currentHour)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(location.toString())))
                .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(12)))
                .andDo(print());
    }
}