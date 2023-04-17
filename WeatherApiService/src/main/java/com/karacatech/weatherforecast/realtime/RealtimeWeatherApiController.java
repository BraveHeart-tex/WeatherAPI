package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.CommonUtility;
import com.karacatech.weatherforecast.GeolocationException;
import com.karacatech.weatherforecast.GeolocationService;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/realtime")
public class RealtimeWeatherApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
    private final GeolocationService geolocationService;
    private final RealtimeWeatherService realtimeWeatherService;

    @Autowired
    public RealtimeWeatherApiController(GeolocationService geolocationService,
                                        RealtimeWeatherService realtimeWeatherService) {
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
    }

    @GetMapping
    public ResponseEntity<?> getRealTimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);

            return ResponseEntity.ok(realtimeWeather);
        } catch (GeolocationException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.notFound().build();
        }

    }
}
