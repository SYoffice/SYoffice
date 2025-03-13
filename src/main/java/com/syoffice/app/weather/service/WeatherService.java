package com.syoffice.app.weather.service;

import com.syoffice.app.weather.domain.WeatherForecastVO;

import java.util.List;
import java.util.Map;

public interface WeatherService {

    void getWeatherInfo();   // 날씨정보를 저장하는 스케줄러

    void getWeatherFcst();   // 일기예보를 저장하는 스케줄러

    // === 당일 기상예보를 가져온다. === //
    List<WeatherForecastVO> todayForecast();
}
