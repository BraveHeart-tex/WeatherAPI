package com.karacatech.weatherforecast.hourly;

import com.karacatech.weatherforecast.CommonUtility;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/hourly")
public class HourlyWeatherController {
    private final GeolocationService geolocationService;
    private final HourlyWeatherService hourlyWeatherService;
    private final ModelMapper modelMapper;

    public HourlyWeatherController(GeolocationService geolocationService,
                                   HourlyWeatherService hourlyWeatherService,
                                   ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.hourlyWeatherService = hourlyWeatherService;
        this.modelMapper = modelMapper;
    }

    private HourlyWeatherListDTO listEntityToDTO(List<HourlyWeather> hourlyWeather) {
        Location location = hourlyWeather.get(0).getId().getLocation();

        HourlyWeatherListDTO resultDTO = new HourlyWeatherListDTO();
        resultDTO.setLocation(location.toString());

        hourlyWeather.forEach(forecast -> {
            HourlyWeatherDTO hourlyWeatherDTO = modelMapper.map(forecast, HourlyWeatherDTO.class);
            resultDTO.addWeatherHourlyDTO(hourlyWeatherDTO);
        });

        return resultDTO;
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
            return ResponseEntity.ok(listEntityToDTO(hourlyForecast));

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

            return ResponseEntity.ok(listEntityToDTO(hourlyForecast));

        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }

    }
}