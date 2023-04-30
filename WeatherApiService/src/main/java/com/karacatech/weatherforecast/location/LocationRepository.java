package com.karacatech.weatherforecast.location;


import com.karacatech.weatherforecast.common.Location;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
     List<Location> findUntrashed();


    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
     Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location  SET trashed = true WHERE code = ?1")
     void trashByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.countryCode = ?1 AND l.cityName = ?2")
    Location findByCountryCodeAndCityName(String countryCode,String cityName);
}
