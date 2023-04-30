package com.karacatech.weatherforecast.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.karacatech.weatherforecast.common.HourlyWeather} entity
 */

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDTO implements Serializable {
    @Range(min = -50, max = 50, message = "Temperature must be between -50 and 50")
    private int temperature;

    @Range(min = 0, max = 100, message = "Precipitation must be between 0 and 100")
    private int precipitation;

    @NotBlank(message = "Status must not be blank")
    @Length(min = 3, max = 50, message = "Status must be between 3 and 50 characters")
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

    public HourlyWeatherDTO precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeatherDTO status(String status) {
        setStatus(status);
        return this;
    }

    public HourlyWeatherDTO hourOfDay(int hour) {
        setHourOfDay(hour);
        return this;
    }

    public HourlyWeatherDTO temperature(int temperature) {
        setTemperature(temperature);
        return this;
    }

    @Override
    public String toString() {
        return "HourlyWeatherDTO{" +
                "temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                ", hourOfDay=" + hourOfDay +
                '}';
    }
}