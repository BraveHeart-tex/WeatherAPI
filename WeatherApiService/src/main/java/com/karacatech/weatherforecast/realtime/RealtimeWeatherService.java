package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException("Location not found: " + location);
        }

        return realtimeWeather;
    }
}
