package com.karacatech.weatherforecast.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.karacatech.weatherforecast.common.HourlyWeather} entity
 */

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDTO implements Serializable {
    private int temperature;
    private int precipitation;
    private String status;
    @JsonProperty("hour_of_day")
    private int hourOfDay;

    public HourlyWeatherDTO() {
    }

    public HourlyWeatherDTO(int temperature, int precipitation,
                            String status, int hourOfDay) {
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.status = status;
        this.hourOfDay = hourOfDay;
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

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourlyWeatherDTO that = (HourlyWeatherDTO) o;
        return temperature == that.temperature && precipitation == that.precipitation && hourOfDay == that.hourOfDay && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, precipitation, status, hourOfDay);
    }
}