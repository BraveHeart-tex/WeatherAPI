package com.karacatech.weatherforecast.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import com.karacatech.weatherforecast.location.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

    private static final String END_POINT_PATH = "/api/v1/realtime";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    GeolocationService geolocationService;

    @MockBean
    RealtimeWeatherService realtimeWeatherService;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws GeolocationException, Exception {

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus404NotFound() throws GeolocationException, Exception {
        Location location = new Location();

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testStatusShouldReturn200OK() throws Exception, GeolocationException {
        Location location = new Location();
        location.setCode("SFCA_USA");
        location.setCityName("San Francisco");
        location.setRegionName("California");
        location.setCountryName("United States of America");
        location.setCountryCode("US");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setPrecipitation(12);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(12);

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);


        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andDo(print());
    }

    @Test
    public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
        String locationCode = "INVALID_CODE";

        Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenThrow(LocationNotFoundException.class);

        String requestURI = END_POINT_PATH + "/" + locationCode;

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
        String locationCode = "BOSTON_US";

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("Boston");
        location.setRegionName("Massachusetts");
        location.setCountryName("United States of America");
        location.setCountryCode("US");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setPrecipitation(12);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(12);

        realtimeWeather.setLocation(location);

        Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);

        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName())))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        String locationCode = "BOSTON_US";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(133);
        realtimeWeather.setHumidity(133);
        realtimeWeather.setPrecipitation(133);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(500);

        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        String locationCode = "ABC_AD";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(5);

        Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather))
                .thenThrow(LocationNotFoundException.class);

        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        String locationCode = "BOSTON_US";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(24);
        realtimeWeather.setHumidity(44);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(15);

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("Boston");
        location.setRegionName("Massachusetts");
        location.setCountryName("United States of America");
        location.setCountryCode("US");

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather))
                .thenReturn(realtimeWeather);

        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();


        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andDo(print());
    }
}