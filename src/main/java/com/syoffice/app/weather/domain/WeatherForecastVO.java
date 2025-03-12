package com.syoffice.app.weather.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastVO {
    private String forecast_no;         // 예보번호(시퀀스)
    private String forecast_date;       // 예보시각
    private String forecast_temp;       // 예상기온
    private String forecast_reh;        // 습도
    private String forecast_mintemp;    // 최저기온
    private String forecast_maxtemp;    // 최고기온
    private String forecast_pop;        // 강수확률
    private String forecast_vec;        // 풍향
    private String forecast_wsd;        // 풍속
}
