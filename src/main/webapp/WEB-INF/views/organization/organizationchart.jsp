<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    


<%
	String ctxPath = request.getContextPath();
%>


<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>

<%-- Bootstrap CSS --%>
<script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" >
 
  <%-- Font Awesome 6 Icons --%>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<style type="text/css">
.highcharts-figure,
.highcharts-data-table table {
    min-width: 360px;
    max-width: 800px;
    margin: 1em auto;
}

.highcharts-data-table table {
    font-family: Verdana, sans-serif;
    border-collapse: collapse;
    border: 1px solid #ebebeb;
    margin: 10px auto;
    text-align: center;
    width: 100%;
    max-width: 500px;
}

.highcharts-data-table caption {
    padding: 1em 0;
    font-size: 12pt;
    color: #555;
}

.highcharts-data-table th {
    font-weight: 600;
    padding: 0.5em;
}

.highcharts-data-table td,
.highcharts-data-table th,
.highcharts-data-table caption {
    padding: 0.5em;
}

.highcharts-data-table thead tr,
.highcharts-data-table tr:nth-child(even) {
    background: #f8f8f8;
}

.highcharts-data-table tr:hover {
    background: #f1f7ff;
}

#container h4 {
    text-transform: none;
    font-size: 14px;
    font-weight: normal;
}

@media screen and (max-width: 600px) {
    #container h4 {
        font-size: 2.3vw;
        line-height: 3vw;
    }
}

</style>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/organization/common.css" />

<script src="<%= ctxPath%>/Highcharts-10.3.1/code/highcharts.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/sankey.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/organization.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/exporting.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/accessibility.js"></script>



<jsp:include page="../main/header.jsp"/>



<%-- 모달 창 --%>
<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document" >
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="userModalLabel" style="margin-left: 2%;">사원 정보</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">X</span>
        </button>
      </div>
      <div class="modal-body" style="margin-left: 5%;">
        <div id="userInfo" style="display: flex;">
        <div style="margin-top: 3%; margin-left: 2%;">
        <img id="userImage" style=" border:solid 1px gray; width: 130px; height: 130px; border-radius: 10px;"/>
        </div>
        <div style="margin-top: 4%; margin-left: 8%;">
            <p>이름&nbsp;&nbsp;&nbsp;<input type="text" id="name" readonly></p>
            <p>소속&nbsp;&nbsp;&nbsp;<input type="text" id="department" readonly></p>
            <p>직급&nbsp;&nbsp;&nbsp;<input type="text" id="grade" readonly></p>
        </div>
        
        </div>
        <div style="margin-top: 3%; margin-left: 2%; line-height: normal;">
            <p>이메일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="mail" style="width:327px; " readonly></p>
            <p>휴대폰&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="tel" style="width:327px;" readonly></p>
            <p>입사일자&nbsp;&nbsp;<input type="text" id="hire_date" style="width:327px; margin-left: 2px; " readonly></p>
        </div>
        
      </div>
    </div>
  </div>
</div>


<div class="common_wrapper">
        <div class="side_menu_wrapper">
        <div style="margin-top: 29%;">
        
        <jsp:include page="./organization.jsp"></jsp:include>
         </div>   
        </div>
        <div class="contents_wrapper">
        
        	<div style="border: solid 0px black; width: 80%; margin: 6% 14% 0 auto;">
        
        	<div id="organization" style="text-align: center; ">
		    <%-- 부서 버튼 (판매부 제외) --%>
		    <c:forEach var="dept" items="${departments}">
		        <c:if test="${dept.DEPT_NAME != '판매부'}">
		            <button type="button"  class="btn btn-light; border border-dark;" data-val="${dept.DEPT_NAME}">
		                ${dept.DEPT_NAME}
		            </button>
		        </c:if>
		    </c:forEach>
		
		    <%-- 지점 버튼 (본사 제외) --%>
		    <c:forEach var="branch" items="${branches}">
		        <c:if test="${branch.BRANCH_NAME != '본사'}">
		            <button type="button"  class="btn btn-light; border border-dark;" data-val="${branch.BRANCH_NAME}">
		                ${branch.BRANCH_NAME}
		            </button>
		        </c:if>
		    </c:forEach>
			</div>

            	
            	<figure class="highcharts-figure">
				    <div  id="container"></div>
				</figure>
			</div>	
        </div>
    </div>


<script type="text/javascript">
	
$(document).ready(function(){
	
    // 각 버튼 클릭 시, data-val 값으로 func_choice 호출
    $("#organization button").click(function(e){
        var selectedVal = $(e.target).data("val");
        func_choice(selectedVal);
    });
    
    // 문서가 로드 되어지면 "임원진" 차트가 보이도록 한다. 
    func_choice("임원진");
    
}); // end of $(document).ready(function(){})--------------------------------
	
 // Function Declaration
 function func_choice(searchTypeVal) {  // searchType의 value 값

	    let deptName = searchTypeVal;
	    let branchName = "";
	    
	    if (searchTypeVal == "강남지점" || searchTypeVal == "강북지점") {  // 강남지점 또는 강북지점을 선택하면 부서명을 "판매부"로 설정(다른지점에 판매부서는 같아서)
	        deptName = "판매부";
	        branchName = searchTypeVal;
	    }
	    
	    else {
	        deptName   = searchTypeVal;
	        branchName = "본사";
	    }
	    
	    $.ajax({
	        url: "<%=request.getContextPath()%>/organization/dataByDept",
	        data: {
	            dept_name: deptName,   
	            branch_name: branchName
	        },
	        dataType: "json",
	        success: function(json) {
	        //  console.log( JSON.stringify(json));
	            /*
	            [{"EXECUTIVE_ID":2025015,"DEPT_NAME":"마케팅부","EMP_ID":2025007,"PROFILE_IMG":"곽철이.png","GRADE_NAME":"주임","BRANCH_NAME":"본사","MANAGER_ID":2025007,"NAME":"곽철이따봉"}
	            ,{"DEPT_NAME":"마케팅부","EMP_ID":2025003,"GRADE_NAME":"사원","BRANCH_NAME":"본사","MANAGER_ID":2025007,"NAME":"실험용"}
	            ,{"DEPT_NAME":"마케팅부","EMP_ID":2025009,"PROFILE_IMG":"펭구.png","GRADE_NAME":"사원","BRANCH_NAME":"본사","MANAGER_ID":2025007,"NAME":"김펭구"}
	            ,{"DEPT_NAME":"마케팅부","EMP_ID":2025010,"PROFILE_IMG":"곽철이 미쳤따.png","GRADE_NAME":"사원","BRANCH_NAME":"본사","MANAGER_ID":2025007,"NAME":"이곽철"}
	            ,{"DEPT_NAME":"마케팅부","EMP_ID":2025011,"PROFILE_IMG":"곽철이 고고.png","GRADE_NAME":"사원","BRANCH_NAME":"본사","MANAGER_ID":2025007,"NAME":"김곽철"}]
	            */

	            let data = [];
	            let nodes = [];
	            let empMap = {};
	            let title;
	            let manager = false;
	            let allnull = false;
	            
	            if(branchName == "본사"){ // 본사면 제목을 부서만
	            	title = deptName;
				}
	            else{	// 강남, 강북지점이면 제목을 지점으로
	            	title = branchName;
	            }
	            
	            json.forEach(emp => {
	                empMap[emp.EMP_ID] = emp.NAME; 
	            });
	            
	            
	            json.forEach(emp => {
	            	
	            	if (branchName == "본사" ) { // 지점이 본사이고, 부서명이 임원진이 아닐때
	            		
	            		 if (emp.MANAGER_ID && emp.EXECUTIVE_ID) { // 매니저 아이디, 임원진아이디 있을때
	                        
	                         if (emp.EXECUTIVE_ID != emp.EMP_ID && empMap[emp.EXECUTIVE_ID]) {
	                             data.push([ empMap[emp.EXECUTIVE_ID], empMap[emp.MANAGER_ID] ]);
	                         }
	                         if (emp.MANAGER_ID != emp.EMP_ID && empMap[emp.MANAGER_ID]) {
	                             data.push([ empMap[emp.MANAGER_ID], emp.NAME ]);
	                         }
	                     } 
	            		 else if (emp.MANAGER_ID && !emp.EXECUTIVE_ID) { // 매니저 아이디있고 없을때
 
	                         if (manager) { 
	                             nodes.push({
	                                 id: "공석",
	                                 name: "공석"
	                             });
	                             manager = true;
	                         }
	                         
	                         data.push(["공석", empMap[emp.MANAGER_ID]]);
	                         
	                         data.push([empMap[emp.MANAGER_ID], emp.NAME]);
	                         
	                     }
	            	
	            		 else if (!emp.MANAGER_ID && emp.EXECUTIVE_ID) { // 매니저 아이디 없고 임원진아이디 있을때
	            			    if (!manager) { 
	            			        nodes.push({
	            			            id: "공석",
	            			            name: "공석"
	            			        });
	            			        manager = true;
	            			    }

           			    // 임원진가 있는 경우 "공석"을 매니저 노드로 연결
           			    if (emp.EXECUTIVE_ID != emp.EMP_ID && empMap[emp.EXECUTIVE_ID]) {
           			        data.push([empMap[emp.EXECUTIVE_ID], "공석"]);
           			    }

           			    // 공석을 통해 사원을 연결
           			    data.push(["공석", emp.NAME]);
           			    
           				}

	            		 else if (!emp.MANAGER_ID && !emp.EXECUTIVE_ID) { // 매니저아이디, 임원진아이디 둘다 없을때
	                    	 
	                         if (!allnull) {
	                             nodes.push({
	                                 id: "공석1",
	                                 name: "공석"
	                             });
	                             nodes.push({
	                                 id: "공석2",
	                                 name: "공석"
	                             });
	                             allnull = true;
	                             
	                             
	                             data.push(["공석1", "공석2"]);  // 먼저 임원진아이디에서 매니저아이디로 연결
	                         }
	                         
	                         data.push(["공석2", emp.NAME]); // 매니저아이디에서 사원으로 연결
	                     } 
	            		 
	            	
	            		 else if (emp.DEPT_NAME == "임원진"){ // 부서가 임원진일때
	 	            		
		            		 if (emp.EXECUTIVE_ID != emp.EMP_ID && empMap[emp.EXECUTIVE_ID]) { 
	                             data.push([empMap[emp.EXECUTIVE_ID], emp.NAME]);   // 임원진아이디 > 사원
	                         }
		            		
		            	}
	            	
	            	}
	                 
	            	
	            	else { // 본사가 아니고 지점일경우
	                     
	                     if (emp.LEADER_ID && emp.LEADER_ID != emp.EMP_ID && empMap[emp.LEADER_ID]) {
	                         data.push([ empMap[emp.LEADER_ID], emp.NAME ]);
	                     }
	            	
	                     
	                 }
	            	
	            	
                    data = [...new Set(data.map(JSON.stringify))].map(JSON.parse); // 데이터 중복 제거
	                
                    nodes.push({
                        id: emp.NAME,
                        name: emp.NAME + "\n" + (branchName != "본사" && emp.LEADER_ID == emp.EMP_ID ? "지점장" : emp.GRADE_NAME),  // 본사는 직급명 지점은 지점장
                        image: `<%= ctxPath %>/resources/profile/\${emp.PROFILE_IMG || "기본이미지.png"}`,
                        color: emp.GRADE_NAME == "대표이사" ? "#808080" : undefined  // 직급이 대표이사는 회색으로 만듬
                    });

	            });
	            
	            
             	 console.log(data);
	            /*
	            ['서영학', '김영학']  '부모', '자식' 관계로 서영학이 김영학, 박영학, 이영학의 부모여서 1-3 로 나옴.
				['서영학', '박영학']
				['서영학', '이영학']
	            */
	            
	       //     console.log(nodes);
	            /*
				{id: '서영학', name: '서영학\n대표이사', image: '/syoffice/resources/profile/'}
				{id: '김영학', name: '김영학\n전무', image: '/syoffice/resources/profile/'}
				{id: '박영학', name: '박영학\n전무', image: '/syoffice/resources/profile/'}
				{id: '이영학', name: '이영학\n전무', image: '/syoffice/resources/profile/'}
	            */
	            

	            Highcharts.chart('container', {
	                chart: { height: 600, inverted: true },
	                title: { text: title },
	                series: [{
	                    type: 'organization',
	                    name: '조직도',
	                    keys: ['from', 'to'],
	                    data: data,
	                    levels: [{
	                        level: 0, color: '#980104', dataLabels: { color: 'white' }
	                    }, {
	                        level: 1, color: '#007ad0', dataLabels: { color: 'white' }
	                    }, {
	                        level: 2, color: '#359154'
	                    }],
	                    nodes: nodes,
	                    colorByPoint: false,
	                    color: '#007ad0',
	                    dataLabels: { color: 'white' },
	                    borderColor: 'white',
	                    nodeWidth: 100,
	                    nodeheight: 100,
	                    nodePadding: 10 
	                }]
	            });
	        },
	        error: function(request, status, error){
		        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		    }
	    });
	}


</script>  

 
