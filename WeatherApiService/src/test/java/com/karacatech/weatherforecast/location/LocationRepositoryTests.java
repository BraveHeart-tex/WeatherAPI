package com.karacatech.weatherforecast.location;

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
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository repository;

    @Test
    public void testAddSuccess() {
        Location location = new Location();
        location.setCode("IST");
        location.setCityName("Istanbul");
        location.setRegionName("Europe");
        location.setCountryCode("TR");
        location.setCountryName("Turkey");
        location.setEnabled(true);

        Location savedLocation = repository.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("IST");
    }

    @Test
    public void testListSuccess() {
        List<Location> untrashedLocations = repository.findUntrashed();
        assertThat(untrashedLocations).isNotEmpty();
        untrashedLocations.forEach(System.out::println);
    }

    @Test
    public void testGetNotFound() {
        String code = "ABCD";
        Location location = repository.findByCode(code);
        assertThat(location).isNull();
    }

    @Test
    public void testGetFound() {
        String code = "NYC_USA";
        Location location = repository.findByCode(code);
        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    public void testTrashSuccess() {
        String code = "IST";
        repository.trashByCode(code);
        Location location = repository.findByCode(code);
        assertThat(location).isNull();
    }
}
