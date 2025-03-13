package com.syoffice.app.weather.model;

import com.syoffice.app.weather.domain.WeatherForecastVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WeatherDAO {

    // === 시간별 기온 정보를 DB에 저장한다. === //
    void addWeatherInfo(Map<String, String> paraMap);

    // === 하루 예보 정보를 DB에 저장한다. === //
    void addWeatherInfoToday(Map<String, String> paraMap);

    // === 예보 테이블 데이터 삭제하기 === //
    void deleteWeatherFcst();

    // === 당일 기상예보를 가져온다. === //
    List<WeatherForecastVO> getTodayForecast();
}
