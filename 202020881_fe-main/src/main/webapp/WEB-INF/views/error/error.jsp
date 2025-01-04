<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<%-- 	<h1>An error occurred</h1>
    <c:if test="${not empty requestScope['jakarta.servlet.error.exception']}">
    	<p><strong>Request URI:</strong> ${requestScope["jakarta.servlet.error.request_uri"]}</p>
    	<p><strong>Status Code:</strong> ${requestScope["jakarta.servlet.error.status_code"]}</p>
    </c:if>
    <c:if test="${not empty requestScope['jakarta.servlet.error.exception']}">
        <p><strong>Exception:</strong> <%= exception.getClass().getName() %></p>
        <p><strong>Exception Message:</strong> <%= exception.getMessage() %></p>
    </c:if> --%>
     <%= exception.getMessage() %>
    <c:if test="${empty requestScope['jakarta.servlet.error.exception']}">
        <p><strong>Error Message:</strong> ${requestScope["jakarta.servlet.error.message"]}</p>
    </c:if>
    
    <c:if test="${not empty errorDetails}">
        <div style="color:red;">
            <h2>검증 오류 목록:</h2>
            <p>${errorDetails}</p>
        </div>
    </c:if>


<body>
  


</body>
</html>
