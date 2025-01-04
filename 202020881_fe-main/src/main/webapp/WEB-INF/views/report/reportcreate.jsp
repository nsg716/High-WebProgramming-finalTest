<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>리포트 데이터 생성</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .report-form {
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 400px;
        }
        .report-form h1 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 10px;
            color: #555;
        }
        .form-group input {
            width: 100%;
            padding: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-group input:focus {
            outline: none;
            border-color: #007bff;
        }
        .form-group button {
            width: 100%;
            padding: 10px;
            border: none;
            background-color: #007bff;
            color: white;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .form-group button:hover {
            background-color: #0056b3;
        }
        .error-message {
            color: #dc3545;
            font-size: 0.8em;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <form action="/nsg716/reportcreate" method="post" class="report-form">
        <h1>리포트 데이터 생성</h1>
        <input type="hidden" id="modelId" name="modelId" value="${model.modelId}">
        <input type="hidden" id="adminId" name="adminId" value="${model.adminId}">
    	<input type="hidden" id="modelName" name="modelName" value="${model.modelName}">
        
        <div class="form-group">
            <label for="reportId">리포트 ID</label>
            <input type="text" id="reportId" name="reportId" value="${reportDTO.reportId}" required>
            <c:if test="${not empty reportIdError}">
                <span class="error-message">${reportIdError}</span>
            </c:if>
        </div>
   
        
        <div class="form-group">
            <label for="reportName">리포트 이름</label>
            <input type="text" id="reportName" name="reportName" value="${reportDTO.reportName}" required>
        </div>
        
        <div class="form-group">
            <label for="usageDate">사용 날짜</label>
            <input type="datetime-local" id="usageDate" name="usageDate" value="${reportDTO.usageDate}" required>
        </div>
      
          
        
        <div class="form-group">
            <label for="accuracy">정확도</label>
            <input type="number" id="accuracy" name="accuracy" step="0.01" value="${reportDTO.accuracy}" required>
        </div>
        
        <div class="form-group">
            <label for="totalProcessingAmount">총 처리량</label>
            <input type="number" id="totalProcessingAmount" name="totalProcessingAmount" value="${reportDTO.totalProcessingAmount}" required>
        </div>
        
        <div class="form-group">
            <label for="averageResponseTime">평균 응답 시간 (s)</label>
            <input type="number" id="averageResponseTime" name="averageResponseTime" step="0.01" value="${reportDTO.averageResponseTime}" required>
        </div>
        
        <div class="form-group">
            <button type="submit">생성하기</button>
        </div>
    </form>
</body>
</html>
