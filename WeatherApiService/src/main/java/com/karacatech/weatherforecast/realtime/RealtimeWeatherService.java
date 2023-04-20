package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.common.RealtimeWeather;
import com.karacatech.weatherforecast.location.LocationNotFoundException;
import com.karacatech.weatherforecast.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository,
                                  LocationRepository locationRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.locationRepository = locationRepository;
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

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException("Location not found with the given location code: " + locationCode);
        }

        realtimeWeather.setLocationCode(locationCode);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setLocation(location);

        if (location.getRealtimeWeather() == null) {
            location.setRealtimeWeather(realtimeWeather);
            Location updatedLocation = locationRepository.save(location);

            return updatedLocation.getRealtimeWeather();
        }

        return realtimeWeatherRepository.save(realtimeWeather);
    }
}
