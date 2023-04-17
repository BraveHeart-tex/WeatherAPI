package com.karacatech.weatherforecast.realtime;

import com.karacatech.weatherforecast.common.RealtimeWeather;
import org.springframework.data.repository.CrudRepository;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {
}
