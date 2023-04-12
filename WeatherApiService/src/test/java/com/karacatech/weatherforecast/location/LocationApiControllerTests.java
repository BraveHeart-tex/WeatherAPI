package com.karacatech.weatherforecast.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karacatech.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

    private static final String END_POINT_PATH = "/api/v1/locations";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocationService locationService;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        Location location = new Location();

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.add(location))
                .thenReturn(location);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NYC_USA"))
                .andExpect(jsonPath("$.city_name").value("New York City"))
                .andExpect(jsonPath("$.region_name").value("New York"))
                .andExpect(jsonPath("$.country_code").value("US"))
                .andExpect(jsonPath("$.country_name").value("United States of America"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andDo(print());
    }

    @Test
    public void testValidateRequestBodyLocationCode() throws Exception {
        Location location = new Location();
        location.setCityName("Ankara");
        location.setRegionName("Ankara");
        location.setCountryCode("TR");
        location.setCountryName("Turkey");
        location.setEnabled(true);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]", is("Location code cannot be null")))
                .andDo(print());
    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception {
        Location location = new Location();
        location.setCode("");
        location.setCityName("Ankara");
        location.setRegionName("Ankara");
        location.setCountryCode("TR");
        location.setCountryName("Turkey");
        location.setEnabled(true);

        String bodyContent = objectMapper.writeValueAsString(location);

        try {
            mockMvc.perform(post(END_POINT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bodyContent))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errors[0]", is("Location code must be between 3 and 12 characters")))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
        Location location = new Location();

        String bodyContent = objectMapper.writeValueAsString(location);

        MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON).content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        assertThat(responseBody).contains("Location code cannot be null");
        assertThat(responseBody).contains("City name cannot be null");
        assertThat(responseBody).contains("Country name cannot be null");
        assertThat(responseBody).contains("Country code cannot be null");
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.getAll())
                .thenReturn(List.of());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200Ok() {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Location location2 = new Location();
        location2.setCode("LON_UK");
        location2.setCityName("London");
        location2.setRegionName("Greater London");
        location2.setCountryCode("GB");
        location2.setCountryName("United Kingdom");
        location2.setEnabled(true);

        Location location3 = new Location();
        location3.setCode("PAR_FR");
        location3.setCityName("Paris");
        location3.setRegionName("Île-de-France");
        location3.setCountryCode("FR");
        location3.setCountryName("France");
        location3.setEnabled(true);

        Mockito.when(locationService.getAll())
                .thenReturn(List.of(location, location2, location3));

        try {
            mockMvc.perform(get(END_POINT_PATH))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].code").value("NYC_USA"))
                    .andExpect(jsonPath("$[0].city_name").value("New York City"))
                    .andExpect(jsonPath("$[0].region_name").value("New York"))
                    .andExpect(jsonPath("$[0].country_code").value("US"))
                    .andExpect(jsonPath("$[0].country_name").value("United States of America"))
                    .andExpect(jsonPath("$[0].enabled").value(true))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetShouldReturn200() {
        Location location = new Location();

        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.getByCode(location.getCode()))
                .thenReturn(location);

        try {
            mockMvc.perform(get(END_POINT_PATH + "/" + location.getCode()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value("NYC_USA"))
                    .andExpect(jsonPath("$.city_name").value("New York City"))
                    .andExpect(jsonPath("$.region_name").value("New York"))
                    .andExpect(jsonPath("$.country_code").value("US"))
                    .andExpect(jsonPath("$.country_name").value("United States of America"))
                    .andExpect(jsonPath("$.enabled").value(true))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetShouldReturn404NotFound() {
        try {
            mockMvc.perform(get(END_POINT_PATH + "/xxxx"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() {
        try {
            mockMvc.perform(post(END_POINT_PATH + "/xxxx"))
                    .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateShouldReturn404NotFound() {
        Location location = new Location();
        location.setCode("RANDOM_CODE");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(location))
                .thenThrow(new LocationNotFoundException("Location not found"));

        try {
            mockMvc.perform(put(END_POINT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(location)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() {
        Location location = new Location();
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(location))
                .thenReturn(location);

        try {
            mockMvc.perform(put(END_POINT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(location)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateShouldReturn200OK() {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(location))
                .thenReturn(location);

        try {
            mockMvc.perform(put(END_POINT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(location)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteShouldReturn204NoContent() {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        try {
            mockMvc.perform(delete(END_POINT_PATH + "/" + location.getCode()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteShouldReturn404NotFound() {

        Mockito.doThrow(new LocationNotFoundException("Location not found"))
                .when(locationService).delete("xxxx");
        try {
            mockMvc.perform(delete(END_POINT_PATH + "/xxxx"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
