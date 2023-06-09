package com.karacatech.weatherforecast;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.karacatech.weatherforecast.common.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
@Service
public class GeolocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);
    private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";
    private IP2Location ipLocator = new IP2Location();

    public GeolocationService() {
        try {
            ipLocator.Open(DBPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = ipLocator.IPQuery(ipAddress);

            if (!"OK".equals(result.getStatus())) {
                throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
            }

            LOGGER.info(result.toString());

            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(),
                    result.getCountryShort());
        } catch (IOException e) {
            throw new GeolocationException("Error querying IP database", e);
        }
    }
}
