package com.karacatech.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import com.karacatech.weatherforecast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {
    @Autowired
    private RealtimeWeatherRepository repository;

    @Test
    public void testUpdate() {
        String locationCode = "BOSTON_US";
        RealtimeWeather realtimeWeather = repository.findById(locationCode).get();

        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(43);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());

        RealtimeWeather updatedRealtimeWeather = repository.save(realtimeWeather);

        assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(32);
    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
        String countryCode = "JP";
        String city = "Tokyo";

        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, city);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByCountryCodeAndCityFound() {
        String countryCode = "US";
        String city = "Boston";

        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, city);

        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocation().getCountryCode()).isEqualTo(countryCode);
        assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(city);
    }
}
