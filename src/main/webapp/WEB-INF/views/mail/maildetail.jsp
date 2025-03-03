<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<jsp:include page="./sidebar.jsp"></jsp:include>

<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/mail/maildetail.css'rel='stylesheet' />
<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/mail/maildetail.js'></script>

    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">

            <div style="margin: 4% 0 4% 0">
            </div>

            <div class="toolbar_wrap">
                <span class="toolbar delete"><i class="fa-regular fa-trash-can"></i>삭제</span>
                <span class="toolbar reply"><i class="fa-solid fa-reply"></i>답장</span>
                <span class="toolbar forward"><i class="fa-solid fa-share"></i>전달</span>
                <span class="toolbar ban"><i class="fa-solid fa-ban"></i>스팸차단</span>
                <span class="toolbar unread">안읽음</span>
            </div>

            <div class="mailInfo_wrap">
                <div class="mail_header">
                    <%-- 메일 제목 --%>
                    <div class="title">
                        <span class="h3">${requestScope.mailVOFileList[0].mail_subject}</span>
                    </div>

                    <%-- 메일 수발신 정보 --%>
                    <table class="table table-borderless">
                        <tr>
                            <th style="width: 8%;">보낸사람</th>
                            <td class="text-left"><span class="user">${requestScope.mailVOFileList[0].sender_name}&lt;${requestScope.mailVOFileList[0].sender_mail}&gt;</span></td>
                        </tr>
                        <tr>
                            <th style="width: 8%;">받는사람</th>
                            <td class="text-left">
                                <c:forEach var="mailVO" items="${requestScope.mailVOList}" varStatus="status">
                                    <c:if test="${mailVO.receivercc == 0}">
                                        <span class="user">${mailVO.receiver_name}&lt;${mailVO.receiver_mail}&gt;</span>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                        <tr class="mail_cc">
                            <th style="width: 8%;">참조</th>
                            <td class="text-left">
                                <c:forEach var="mailVO" items="${requestScope.mailVOList}" varStatus="status">
                                    <c:if test="${mailVO.receivercc == 1}">
                                        <span class="user">${mailVO.receiver_name}&lt;${mailVO.receiver_mail}&gt;</span>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 8%;">발송시각</th>
                            <td class="text-left"><span class="date">${requestScope.mailVOFileList[0].mail_senddate}</span></td>
                        </tr>
                    </table>
                </div>

                <%-- 첨부파일 --%>
                <c:if test="${requestScope.mailVOFileList[0].atmail_no != -1}">
                    <div class="attachment_wrap">
                        <span>첨부파일</span>
                        <div style="margin-top: 2%;">
                            <table class="table text-secondary table-hover" style="width: 40%; border: 1px solid #ccc;">
                                <c:forEach var="attachFile" items="${requestScope.mailVOFileList}" varStatus="status">
                                    <tr class="file_info">
                                        <td>
                                            <input type="hidden" name="atmail_no" value="${attachFile.atmail_no}" />
                                            ${attachFile.atmail_orgfilename}<i style="float: right; cursor: pointer;" class="fa-solid fa-download"></i>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                </c:if>

                <%-- 본문 시작 --%>

                <div class="content_wrap">
                    ${requestScope.mailVOFileList[0].mail_content}
                </div>

            </div>

        </div>
    </div>
</div>

<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	    value="${pageContext.request.contextPath}" />
<input type="hidden" id="mail_no" 	value="${requestScope.mailVOFileList[0].mail_no}" />
<input type="hidden" id="sender" 	value="${requestScope.mailVOFileList[0].sender}" />