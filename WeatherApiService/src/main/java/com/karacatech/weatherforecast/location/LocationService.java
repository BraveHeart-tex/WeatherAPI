package com.karacatech.weatherforecast.location;

import com.karacatech.weatherforecast.common.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location add(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getAll() {
        return locationRepository.findUntrashed();
    }

    public Location getByCode(String code) {
        return locationRepository.findByCode(code);
    }

    public Location update(Location locationInRequest) {
        String code = locationInRequest.getCode();

        Location locationInDB = locationRepository.findByCode(code);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given code: " + code);
        }

        locationInDB.setCityName(locationInRequest.getCityName());
        locationInDB.setRegionName(locationInRequest.getRegionName());
        locationInDB.setCountryCode(locationInRequest.getCountryCode());
        locationInDB.setCountryName(locationInRequest.getCountryName());
        locationInDB.setEnabled(locationInRequest.isEnabled());

        return locationRepository.save(locationInDB);
    }

    public void delete(String code) {
        Location location = locationRepository.findByCode(code);

        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + code);
        }

        locationRepository.trashByCode(code);
    }
}
