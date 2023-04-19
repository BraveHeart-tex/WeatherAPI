package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
        return Optional
                .ofNullable(realtimeWeatherRepository.findByCountryCodeAndCity(location.getCountryCode(), location.getCityName()))
                .orElseThrow(() -> new LocationNotFoundException(
                        "Location not found with the given city name and country code: " + location)
                );
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
        return Optional
                .ofNullable(realtimeWeatherRepository.findByLocationCode(locationCode))
                .orElseThrow(() -> new LocationNotFoundException(
                        "Location not found with the given location code: " + locationCode)
                );
    }
}
