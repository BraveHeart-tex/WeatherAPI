package com.karacatech.weatherforecast.hourly;

import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.HourlyWeatherID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherID> {
    @Query("""
                SELECT hw FROM HourlyWeather hw
                WHERE hw.id.location.code = ?1 AND hw.id.hourOfDay = ?2
                AND hw.id.location.trashed = false
            """)
    public List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);
}
