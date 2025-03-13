package com.syoffice.app.weather.controller;

import com.syoffice.app.weather.domain.WeatherForecastVO;
import com.syoffice.app.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/weather/*")
public class WeatherController {

    @Autowired
    private WeatherService service;

    // === 날씨 정보 Xml 파일 === //
    @GetMapping("weatherXML")
    public String weatherXML() {
        return "weather/weather_xml";
    }// end of public String weatherXML() ----------------


    // === 일기예보 정보를 가져온다 === //
    @GetMapping("forecast")
    @ResponseBody
    public List<WeatherForecastVO> todayForecast() {
        return service.todayForecast();
    }// end of public List<Map<String, String>> forecast() ------------------------
}
