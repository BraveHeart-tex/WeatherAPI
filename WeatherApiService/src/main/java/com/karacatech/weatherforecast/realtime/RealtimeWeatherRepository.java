package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.common.RealtimeWeather;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {
    @Query("select r from RealtimeWeather r where r.location.countryCode = ?1 and r.location.cityName = ?2")
    public RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);
}
