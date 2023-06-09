package com.karacatech.weatherforecast.common;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class HourlyWeatherID implements Serializable {
    private int hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;

    public HourlyWeatherID() {
    }

    public HourlyWeatherID(int hourOfDay, Location location) {
        this.hourOfDay = hourOfDay;
        this.location = location;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
