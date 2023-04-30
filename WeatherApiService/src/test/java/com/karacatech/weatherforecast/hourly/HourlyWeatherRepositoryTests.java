package com.karacatech.weatherforecast.hourly;

import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {

    @Autowired
    private HourlyWeatherRepository hourlyWeatherRepository;

    @Test
    public void testAdd() {
        String locationCode = "IST_TR";
        int hourOfDay = 12;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather()
                .location(location)
                .hourOfDay(hourOfDay)
                .temperature(10)
                .precipitation(15)
                .status("Sunny");

        HourlyWeather updatedForecast = hourlyWeatherRepository.save(forecast);

        assertThat(updatedForecast).isNotNull();
        assertThat(updatedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedForecast.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete() {
        String locationCode = "IST_TR";
        int hourOfDay = 12;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather()
                .location(location)
                .hourOfDay(hourOfDay)
                .temperature(10)
                .precipitation(15)
                .status("Sunny");

        hourlyWeatherRepository.delete(forecast);

        HourlyWeather hourlyWeather = hourlyWeatherRepository.findById(forecast.getId()).orElse(null);
        assertThat(hourlyWeather).isNull();
    }

    @Test
    public void findByLocationCodeFound() {
        String locationCode = "IST_TR";
        int currentHour = 8;

        List<HourlyWeather> hourlyForecast = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);

        assertThat(hourlyForecast).isNotNull();
        assertThat(hourlyForecast.size()).isEqualTo(1);
    }

    @Test
    public void findByLocationCodeNotFound() {
        String locationCode = "IST_TR";
        int currentHour = 10;

        List<HourlyWeather> hourlyForecast = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);

        assertThat(hourlyForecast).isEmpty();
    }
}
