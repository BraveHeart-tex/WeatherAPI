package com.karacatech.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "weather_hourly")
public class HourlyWeather {

    @EmbeddedId
    private HourlyWeatherID id = new HourlyWeatherID();

    private int temperature;
    private int precipitation;

    @Column(length = 50)
    private String status;

    public HourlyWeatherID getId() {
        return id;
    }

    public void setId(HourlyWeatherID id) {
        this.id = id;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HourlyWeather temperature(int temperature) {
        setTemperature(temperature);
        return this;
    }

    public HourlyWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public HourlyWeather location(Location location) {
        this.id.setLocation(location);
        return this;
    }

    public HourlyWeather id(Location location, int hour) {
        this.id.setHourOfDay(hour);
        this.id.setLocation(location);
        return this;
    }
}