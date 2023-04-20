package com.karacatech.weatherforecast.location;

import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;

import com.karacatech.weatherforecast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testAddSuccess() {
        Location location = new Location();
        location.setCode("IST");
        location.setCityName("Istanbul");
        location.setRegionName("Europe");
        location.setCountryCode("TR");
        location.setCountryName("Turkey");
        location.setEnabled(true);

        Location savedLocation = locationRepository.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("IST");
    }

    @Test
    public void testListSuccess() {
        List<Location> untrashedLocations = locationRepository.findUntrashed();
        assertThat(untrashedLocations).isNotEmpty();
        untrashedLocations.forEach(System.out::println);
    }

    @Test
    public void testGetNotFound() {
        String code = "ABCD";
        Location location = locationRepository.findByCode(code);
        assertThat(location).isNull();
    }

    @Test
    public void testGetFound() {
        String code = "NYC_USA";
        Location location = locationRepository.findByCode(code);
        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    public void testTrashSuccess() {
        String code = "IST";
        locationRepository.trashByCode(code);
        Location location = locationRepository.findByCode(code);
        assertThat(location).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData() {
        String locationCode = "BOSTON_US";
        Location location = locationRepository.findByCode(locationCode);

        RealtimeWeather realtimeWeather = location.getRealtimeWeather();

        if (realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(location);
            location.setRealtimeWeather(realtimeWeather);
        }

        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(60);
        realtimeWeather.setPrecipitation(70);
        realtimeWeather.setStatus("Sunny");
        realtimeWeather.setWindSpeed(10);
        realtimeWeather.setLastUpdated(new Date());

        Location updatedLocation = locationRepository.save(location);

        assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(locationCode);
    }

    @Test
    public void testAddHourlyWeatherData() {
        Location location = locationRepository.findById("IST_TR").get();

        List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();

        HourlyWeather forecastOne = new HourlyWeather().id(location, 8)
                .temperature(7)
                .precipitation(10)
                .status("Cold");

        HourlyWeather forecastTwo = new HourlyWeather()
                .location(location)
                .temperature(5)
                .precipitation(8)
                .status("Cold");

        listHourlyWeather.add(forecastOne);
        listHourlyWeather.add(forecastTwo);

        Location updatedLocation = locationRepository.save(location);

        assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
        assertThat(updatedLocation.getListHourlyWeather().size()).isEqualTo(2);
    }
}
