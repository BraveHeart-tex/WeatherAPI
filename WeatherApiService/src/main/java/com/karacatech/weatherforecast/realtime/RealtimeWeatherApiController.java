package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.CommonUtility;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/realtime")
public class RealtimeWeatherApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
    private final GeolocationService geolocationService;
    private final RealtimeWeatherService realtimeWeatherService;
    private final ModelMapper modelMapper;

    @Autowired
    public RealtimeWeatherApiController(GeolocationService geolocationService,
                                        RealtimeWeatherService realtimeWeatherService,
                                        ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getRealTimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);


            RealtimeWeatherDTO DTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(DTO);
        } catch (GeolocationException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error(ipAddress);

            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeByLocationCode(@PathVariable("locationCode") String locationCode) {
        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);

            RealtimeWeatherDTO DTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(DTO);
        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.notFound().build();
        }
    }
}
