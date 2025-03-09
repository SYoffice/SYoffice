

window.onload = () => {
    showWeather();
}// end of window.onload = () => {} -----------------------


function showWeather(){
		
    $.ajax({
        url:"/syoffice/weather/weatherXML",
        type:"get",
        dataType:"xml",
        success:function(xml){
            const rootElement = $(xml).find(":root");	// xml 파일의 최상단 태그
        //	console.log("확인용 : " + $(rootElement).prop("tagName") );	// 태그의 요소 중 태그명을 알아온다.
            // 확인용 : current
            
            const weather = rootElement.find("weather");	// root 태그에서 태그네임이 weather 인 것을 찾는다.
            //const updateTime = $(weather).attr("year")+"년 "+$(weather).attr("month")+"월 "+$(weather).attr("day")+"일 "+$(weather).attr("hour")+"시"; 
            const updateTime = $(weather).attr("hour")+"시"; 
            // weather 태그에서 year, month, day, hour 속성의 값을 찾는다.
        //	console.log(updateTime);
            // 2024년 07월 19일 12시
            
            const localArr = rootElement.find("local");		// root 태그에서 태그네임이 local 인 것을 찾는다. 태그를 배열에 담아준 것.
        //	console.log("지역개수 : " + localArr.length);
            // 지역개수 : 97
            
            let html = "<div style='margin: 25% auto; border: solid 0px red;'>";
                html += "<span style='font-weight:bold;'>"+updateTime+"</span>&nbsp;기준";
                html += "<span style='margin-left: 7%; color:blue; cursor:pointer; font-size:9pt;' onclick='javascript:showWeather();'>업데이트</span><br/><br/>";
                html += "<table class='table' align='center'>";
                html += "<tr rowspan='2'>";
                
            
            // ====== XML 을 JSON 으로 변경하기  시작 ====== //
            var jsonObjArr = [];
            // ====== XML 을 JSON 으로 변경하기  끝 ====== //    
            
            for(let i=0; i<localArr.length; i++){
                
                let local = $(localArr).eq(i);
                /* .eq(index) 는 선택된 요소들을 인덱스 번호로 찾을 수 있는 선택자이다. 
                      마치 배열의 인덱스(index)로 값(value)를 찾는 것과 같은 효과를 낸다.
                */
                
               //console.log( $(local).text() + " stn_id:" + $(local).attr("stn_id") + " icon:" + $(local).attr("icon") + " desc:" + $(local).attr("desc") + " ta:" + $(local).attr("ta") ); 
              //	속초 stn_id:90 icon:03 desc:구름많음 ta:29.0
              //	
                
                let icon = $(local).attr("icon");  
                if(icon == "") {
                    icon = "없음";
                }
                
                if ($(local).attr("stn_id") == '108') {
                    html += "<th>"+$(local).text()+"특별시</th>";
                    html += "<th>기온</th>";
                    html += "</tr>";

                    html += "<tr>";
                    html += "<td><img src='/syoffice/images/weather/"+icon+".png' />"+$(local).attr("desc")+"</td><td style='padding-top: 15%;'>"+$(local).attr("ta")+"℃</td>";
                    html += "</tr>";
                }
                
                // ====== XML 을 JSON 으로 변경하기  시작 ====== //
                var jsonObj = {"locationName":$(local).text(), "ta":$(local).attr("ta")};
                   
                jsonObjArr.push(jsonObj);
                // ====== XML 을 JSON 으로 변경하기  끝 ====== //
                
            }// end of for------------------------ 
            
            html += "</table>";
            html += "</div>";
            
            $("div.calendar-box").html(html);
            
        },// end of success: function(xml){ }------------------
        
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
    
}// end of function showWeather()--------------------