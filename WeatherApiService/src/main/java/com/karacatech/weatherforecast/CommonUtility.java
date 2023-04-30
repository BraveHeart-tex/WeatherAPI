package com.karacatech.weatherforecast;

import com.karacatech.weatherforecast.common.HourlyWeather;
import com.karacatech.weatherforecast.common.Location;
import com.karacatech.weatherforecast.hourly.HourlyWeatherDTO;
import com.karacatech.weatherforecast.hourly.HourlyWeatherListDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommonUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
    private static final ModelMapper modelMapper = new ModelMapper();


    public static String getIPAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        LOGGER.info("IP Address: {}", ipAddress);

        return ipAddress;
    }

    public static HourlyWeatherListDTO listEntityToDTO(List<HourlyWeather> hourlyWeather) {
        Location location = hourlyWeather.get(0).getId().getLocation();

        HourlyWeatherListDTO resultDTO = new HourlyWeatherListDTO();
        resultDTO.setLocation(location.toString());

        hourlyWeather.forEach(forecast -> {
            HourlyWeatherDTO hourlyWeatherDTO = modelMapper.map(forecast, HourlyWeatherDTO.class);
            resultDTO.addWeatherHourlyDTO(hourlyWeatherDTO);
        });

        return resultDTO;
    }

    public static List<HourlyWeather> listDTOToEntity(List<HourlyWeatherDTO> listDTO) {
        List<HourlyWeather> listEntity = new ArrayList<>();

        listDTO.forEach(dto -> listEntity.add(modelMapper.map(dto, HourlyWeather.class)));

        return listEntity;
    }
}
