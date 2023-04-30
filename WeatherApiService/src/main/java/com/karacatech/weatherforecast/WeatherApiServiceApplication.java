package com.karacatech.weatherforecast;

import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.hourly.HourlyWeatherDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiServiceApplication {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        var typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
        typeMap.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
