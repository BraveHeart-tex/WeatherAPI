package com.karacatech.weatherforecast.hourly;

import com.karacatech.weatherforecast.BadRequestException;
import com.karacatech.weatherforecast.CommonUtility;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/v1/hourly")
@Validated
public class HourlyWeatherController {
    private final GeolocationService geolocationService;
    private final HourlyWeatherService hourlyWeatherService;

    public HourlyWeatherController(GeolocationService geolocationService,
                                   HourlyWeatherService hourlyWeatherService) {
        this.geolocationService = geolocationService;
        this.hourlyWeatherService = hourlyWeatherService;
    }


    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            Location locationFromIP = geolocationService.getLocation(ipAddress);

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);


            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(CommonUtility.listEntityToDTO(hourlyForecast));

        } catch (NumberFormatException | GeolocationException ex) {
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable String locationCode,
                                                              HttpServletRequest request) {
        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(CommonUtility.listEntityToDTO(hourlyForecast));

        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateHourlyForecastByLocationCode(@PathVariable String locationCode,
                                                                @RequestBody @Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException {
        if (listDTO.isEmpty()) {
            throw new BadRequestException("The request body is empty. Please provide hourly weather forecast data");
        }


        List<HourlyWeather> listHourlyWeather = CommonUtility.listDTOToEntity(listDTO);

        listHourlyWeather.forEach(System.out::println);

        try {
            List<HourlyWeather> updatedHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, listHourlyWeather);

            return ResponseEntity.ok(CommonUtility.listEntityToDTO(updatedHourlyWeather));
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }

    }
}