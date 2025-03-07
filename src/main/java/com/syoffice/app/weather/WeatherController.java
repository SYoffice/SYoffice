package com.syoffice.app.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather/*")
public class WeatherController {

    // === 날씨 정보 Xml 파일 === //
    @GetMapping("weatherXML")
    public String weatherXML() {
        return "weather/weather_xml";
    }// end of public String weatherXML() ----------------

}
