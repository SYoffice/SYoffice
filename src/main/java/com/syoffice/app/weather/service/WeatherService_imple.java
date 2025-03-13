package com.syoffice.app.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.syoffice.app.weather.domain.WeatherForecastVO;
import com.syoffice.app.weather.model.WeatherDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
public class WeatherService_imple implements WeatherService {


    @Autowired
    private WeatherDAO dao;

    @Scheduled(cron="0 15 * * * *")
//    @Scheduled(fixedDelay = 100000)
    public void getWeatherInfo() {
        try {
            StringBuilder result = new StringBuilder();

            String urlStr = "http://www.kma.go.kr/XML/weather/sfc_web_map.xml";     // 날씨 정보가 담긴 url

            URL url = new URL(urlStr.toString());   // java.net.URL

            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");      // URL 요청(GET 방식으로)

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

            String returnLine;

            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine);
                //result.append(returnLine+"\n");
            }
            urlconnection.disconnect();

            String json = xmlToJson(result.toString());

            JSONObject jsonObj = new JSONObject(json);

            jsonObj = jsonObj.getJSONObject("current");
            jsonObj = jsonObj.getJSONObject("weather");     // 날씨정보만

//            System.out.println(jsonObj.toString());

            int year = jsonObj.getInt("year");
            String month = jsonObj.getString("month");
            int day = jsonObj.getInt("day");
            int hour = jsonObj.getInt("hour");
            String date = year + "-" + month + "-" + day + " " + hour + ":00:00";     // DB 용 날짜 형태

            JSONArray jsonArray = jsonObj.getJSONArray("local");    // 지역정보

            String city = "";       // 도시명
            float temp = 0;           // 온도
            String desc = "";       // 날씨상태

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);

                if (108 == jsonObj.getInt("stn_id")) {
                    // 서울의 정보일 경우
                    city = jsonObj.getString("content") + "특별시";
                    temp = jsonObj.getFloat("ta");
                    desc = jsonObj.getString("desc");

                }
            }// end of for() --------------


            Map<String, String> paraMap = new HashMap<String, String>();

            paraMap.put("city", city);
            paraMap.put("date", date);
            paraMap.put("desc", desc);
            paraMap.put("temp", String.valueOf(temp));

            dao.addWeatherInfo(paraMap);

//            System.out.println(jsonObj.toString());
//            System.out.println(city);
//            System.out.println(String.valueOf(temp));
//            System.out.println(desc);
//            System.out.println(date);


//            return String.valueOf(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return null;
    }// end of public void getWeatherInfo() throws Exception -------------------------

    // === xml을 json 으로 변환해주는 메소드 === //
    public String xmlToJson(String str) {

        String output = "";
        try{
            String xml = str;
            JSONObject jObject = XML.toJSONObject(xml);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object json = mapper.readValue(jObject.toString(), Object.class);
            output = mapper.writeValueAsString(json);
//            System.out.println(output);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }// end of public String xmlToJson(String str) ------------------





    @Scheduled(cron="0 15 4 * * *")
//    @Scheduled(fixedDelay = 100000)
    public void getWeatherFcst() {  // 날씨 예보 정보를 가져온다
        try {

            dao.deleteWeatherFcst();

            Calendar calendar = Calendar.getInstance();
            int year    = calendar.get(Calendar.YEAR);
            int months  = calendar.get(Calendar.MONTH)+1;
            int day     = calendar.get(Calendar.DATE)-1;

            String month = (months < 10)? "0" + months : String.valueOf(months);
            String days  = (day < 10)? "0" + day : String.valueOf(day);

            String date = year + "" + month + "" + days;

//            System.out.println(date);
//            20250310

            // URL 작성용 StringBuilder
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=49XUbpLGuYp%2F%2F4lkKweV8dOimUqK9tqDMT%2BbOpnm%2FcsFt3NXFgLbTx50K4hbajGmatmOJpBcVWOtZGob%2B4I6sg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("290", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /* 예보날짜(어제로 설정) */
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("2300", "UTF-8")); /* 기준시각 */
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("59", "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("126", "UTF-8")); /*예보지점의 Y 좌표값*/

            // HTTP URL 요청
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

//            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
//            System.out.println(sb.toString());

            JSONObject jsonObj = new JSONObject(sb.toString());
            jsonObj = jsonObj.getJSONObject("response");
            jsonObj = jsonObj.getJSONObject("body");
            jsonObj = jsonObj.getJSONObject("items");

            JSONArray jsonArray = jsonObj.getJSONArray("item");
//            System.out.println(jsonArray.toString());

            Map<String, String> paraMap = new HashMap<>();

            String TMP ="";     // 현재기온
            String TMN ="";     // 최저기온
            String TMX ="";     // 최고기온
            String WSD ="";     // 풍속
            String VEC ="";     // 풍향
            String REH ="";     // 습도
            String POP ="";     // 강수량
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String fcstDate = jsonObject.getString("fcstDate");     // 예보일자
                String fcstTime = jsonObject.getString("fcstTime");     // 예보시간

                String dateTime = fcstDate +" "+ fcstTime.substring(0,2) +":00:00";     // DB 용 시간

                String category = jsonObject.getString("category");     // 예보항목
                String fcstValue = jsonObject.getString("fcstValue");   // 예보값



                if ("UUU".equalsIgnoreCase(category) || "VVV".equalsIgnoreCase(category) || "SKY".equalsIgnoreCase(category) || "PTY".equalsIgnoreCase(category) ||
                    "WAV".equalsIgnoreCase(category) || "PCP".equalsIgnoreCase(category) || "SNO".equalsIgnoreCase(category)) {
                    // 필요한 것 외 나머지면 안한다.
                    continue;
                }


                if ("TMN".equalsIgnoreCase(category)) {
//                    System.out.println("최저기온: "+ fcstValue);
                    // 최저기온: 3.0
//                    paraMap.put("TMN", fcstValue);   // 최저기온
                    TMN = fcstValue;
                }

                if ("TMX".equalsIgnoreCase(category)) {
//                    System.out.println("최고기온: "+ fcstValue);
                    // 최고기온: 15.0
//                    paraMap.put("TMX", fcstValue);   // 최고기온
                    TMX = fcstValue;
                }

                if (dateTime.equals(paraMap.get("dateTime"))) {
                    continue;
                }

                if ("TMP".equalsIgnoreCase(category)) {
//                    System.out.println("현재기온: "+ fcstValue);
//                    System.out.println("현재기온: "+ temp);
                    // 현재기온: 11
//                    paraMap.put("TMP", fcstValue);   // 현재기온
                    TMP = fcstValue;
                }

                if ("WSD".equalsIgnoreCase(category)) {
//                    System.out.println("풍속: "+ fcstValue);
                    // 풍속: 1
//                    paraMap.put("WSD", fcstValue);   // 풍속
                    WSD = fcstValue;
                }

                if ("VEC".equalsIgnoreCase(category)) {
                    // (풍향값 + 22.5 * 0.5) / 22.5) = 변환값(소수점 이하 버림)
                    int vec = (int) (Math.floor(Integer.parseInt(fcstValue) + 22.5 * 0.5) / 22.5);  // 소수점은 버림

                    if (vec % 2 != 0) {
                        vec += 1;
                    }
//                    System.out.println("풍향값: "+ vec);
//                    paraMap.put("VEC", String.valueOf(vec));   // 풍향값
                    VEC = String.valueOf(vec);
                }

                if ("REH".equalsIgnoreCase(category)) {
//                    System.out.println("습도: "+ fcstValue);
//                    paraMap.put("REH", fcstValue);   // 습도
                    REH = fcstValue;
                }

                if ("POP".equalsIgnoreCase(category)) {
//                    System.out.println("강수량: "+ fcstValue);
                    // 풍속: 1
//                    paraMap.put("POP", fcstValue);   // 강수량
                    POP = fcstValue;
                }



                if (!"".equals(TMP) && !"".equals(WSD) && !"".equals(VEC) && !"".equals(REH) && !"".equals(POP)) {
                    // 모든 항목이 다 들어있다면
                    paraMap.put("dateTime", dateTime);
                    paraMap.put("TMP", TMP);    // 현재기온
                    paraMap.put("TMN", TMN);    // 최저기온
                    paraMap.put("TMX", TMX);    // 최고기온
                    paraMap.put("WSD", WSD);    // 풍속
                    paraMap.put("VEC", VEC);    // 풍향
                    paraMap.put("REH", REH);    // 습도
                    paraMap.put("POP", POP);    // 습도

                    // 변수 초기화
                    dateTime = "";
                    TMP = "";
                    TMN = "";
                    TMX = "";
                    WSD = "";
                    VEC = "";
                    REH = "";
                    POP = "";

//                    System.out.println("paraMap:"+ paraMap.toString());
                    /*
                        paraMap:{dateTime=20250311 00:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=6, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 01:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=6, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 02:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=6, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 03:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 04:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 05:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 06:00:00, POP=0, TMN=, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 07:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 08:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=5, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 09:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=7, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 10:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=9, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 11:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=11, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 12:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=13, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 13:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=14, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 14:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=14, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 15:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=15, WSD=0.4, TMX=}
                        paraMap:{dateTime=20250311 16:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=15, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 17:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=15, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 18:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=14, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 19:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=12, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 20:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=12, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 21:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=11, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 22:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=10, WSD=0.4, TMX=15.0}
                        paraMap:{dateTime=20250311 23:00:00, POP=0, TMN=3.0, REH=70, VEC=2, TMP=10, WSD=0.4, TMX=15.0}
                     */

                    dao.addWeatherInfoToday(paraMap);       // 하루 날씨예보를 DB에 저장
                }

            }// end of for() -----------------------


        } catch (Exception e) {
            e.printStackTrace();
        }

    }// end of public void getWeatherInfo() throws Exception -------------------------


    // === 당일 기상예보를 가져온다. === //
    @Override
    public List<WeatherForecastVO> todayForecast() {
        return dao.getTodayForecast();
    }// end of public List<Map<String, String>> todayForecast() --------------------------


    //    @Scheduled(fixedDelay = 100000)
    public void getHoliday() {
        try {

            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            int months = calendar.get(Calendar.MONTH)+1;

            String month ="";

            if (months < 10) {
                month = "0" + months;
            }
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=49XUbpLGuYp%2F%2F4lkKweV8dOimUqK9tqDMT%2BbOpnm%2FcsFt3NXFgLbTx50K4hbajGmatmOJpBcVWOtZGob%2B4I6sg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
            urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
//            System.out.println(sb.toString());
//
            JSONObject jsonObj = new JSONObject(sb.toString());
            jsonObj = jsonObj.getJSONObject("response");
            jsonObj = jsonObj.getJSONObject("body");
            jsonObj = jsonObj.optJSONObject("items", null);     // key가 items인 JSON Object 를 가져오는데 값이 없을 경우 null 이다.
//            System.out.println("jsonObj: "+jsonObj);
            /*
                jsonObj: null
                jsonObj: {"item":[{"dateName":"삼일절","locdate":20250301,"dateKind":"01","isHoliday":"Y","seq":1}
                        ,{"dateName":"대체공휴일","locdate":20250303,"dateKind":"01","isHoliday":"Y","seq":1}]}
             */
            if (jsonObj != null) {
                JSONArray jsonArray = jsonObj.getJSONArray("item");
//                System.out.println(jsonArray.toString());
                /*
                    [{"dateKind":"01","dateName":"삼일절","isHoliday":"Y","locdate":20250301,"seq":1}
                    ,{"dateKind":"01","dateName":"대체공휴일","isHoliday":"Y","locdate":20250303,"seq":1}]
                 */
                List<Map<String, String>> holidayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String holiday = obj.getString("isHoliday");     // 공휴일 여부

                    if ("Y".equalsIgnoreCase(holiday)) {
                        // 해당일이 공휴일인 경우
                        String dateName = obj.getString("dateName");            // 공휴일명
                        String locdate = String.valueOf(obj.getInt("locdate")); // 일자

                        Map<String, String> holidayMap = new HashMap<>();
                        holidayMap.put("dateName", dateName);
                        holidayMap.put("locdate", locdate);

                        holidayList.add(holidayMap);
                    }
                }// end of for() -------------------------

                System.out.println(holidayList.toString());
                // [{dateName=삼일절, locdate=20250301}, {dateName=대체공휴일, locdate=20250303}]
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// end of public void getHoliday() ----------------------------


}
