package com.karacatech.weatherforecast.location;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String locationCode) {
        super("No location found with the given code: " + locationCode + ".");
    }

    public LocationNotFoundException(String locationCode, String cityName) {
        super("No location found with the given code: " + locationCode + " and city name: " + cityName + ".");
    }
}
