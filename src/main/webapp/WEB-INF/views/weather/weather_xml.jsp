<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%><%-- trimDirectiveWhitespaces="true" 공백이 있다면 그것을 지운다. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	http://www.kma.go.kr/XML/weather/sfc_web_map.xml 사이트에서 날씨정보를 가져오나
	나오지 않을 것을 대비해
	내 것처럼 만들어준다.
 --%>
<c:import url="https://www.kma.go.kr/XML/weather/sfc_web_map.xml" charEncoding="UTF-8"/>


