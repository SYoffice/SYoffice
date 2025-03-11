package com.syoffice.app.attendance.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service // ✅ 스프링 서비스 등록
public class HolidayService {

    private List<String> holidayList = new ArrayList<>(); 

    @Scheduled(cron = "0 0 3 * * ?") 
    public void getHoliday() {
        try {
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            int month = calendar.get(Calendar.MONTH) + 1;
            String monthStr = (month < 10) ? "0" + month : String.valueOf(month);

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=49XUbpLGuYp%2F%2F4lkKweV8dOimUqK9tqDMT%2BbOpnm%2FcsFt3NXFgLbTx50K4hbajGmatmOJpBcVWOtZGob%2B4I6sg%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(monthStr, "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

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

            JSONObject jsonObj = new JSONObject(sb.toString());
            jsonObj = jsonObj.getJSONObject("response").getJSONObject("body").optJSONObject("items", null);

            if (jsonObj != null) {
                JSONArray jsonArray = jsonObj.getJSONArray("item");
                List<String> updatedHolidays = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if ("Y".equalsIgnoreCase(obj.getString("isHoliday"))) {
                        String locdate = String.valueOf(obj.getInt("locdate"));
                        String formattedDate = locdate.substring(0, 4) + "-" + locdate.substring(4, 6) + "-" + locdate.substring(6, 8);
                        updatedHolidays.add(formattedDate);
                    }
                }

                synchronized (holidayList) { 
                    holidayList.clear();
                    holidayList.addAll(updatedHolidays);
                }

                System.out.println("[INFO] 공휴일 리스트 업데이트 완료: " + holidayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특정 날짜가 공휴일인지 확인하는 메서드 추가
    public boolean isHoliday(String date) {
        synchronized (holidayList) {
            return holidayList.contains(date);
        }
    }
}
