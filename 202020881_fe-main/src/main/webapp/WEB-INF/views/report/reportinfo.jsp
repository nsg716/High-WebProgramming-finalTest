<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>보고서 정보</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
        }

        .report-info-container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }

        p {
            margin: 10px 0;
            padding: 10px;
            border-bottom: 1px solid #eee;
        }

        .btn-group {
            margin-top: 20px;
            text-align: center;
        }

        .btn {
            padding: 10px 20px;
            margin: 0 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .btn-edit {
            background-color: #007bff;
            color: white;
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .error-message {
            color: #dc3545;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="report-info-container">
    <h1>보고서 정보</h1>
    <c:if test="${not empty reportInfo}">
        <p>보고서 ID: ${reportInfo.reportId}</p>
        <p>모델 ID: ${reportInfo.model}</p>
        <p>관리자 ID: ${reportInfo.admin}</p>
        <p>사용자 ID: ${reportInfo.user}</p>
        <p>보고서 이름: ${reportInfo.reportName}</p>
        <p>모델 이름: ${reportInfo.modelName}</p>
        <p>사용 일시: ${reportInfo.usageDate}</p>
	    <p>정확도: ${reportInfo.accuracy} % </p>
        <p>총 처리 양: ${reportInfo.totalProcessingAmount} </p>
        <p>평균 응답 시간: ${reportInfo.averageResponseTime} (s)</p>
        <p>생성일: ${reportInfo.createDate}</p>
        <p>수정일: ${reportInfo.updateDate}</p>

    </c:if>

    <c:if test="${empty reportInfo}">
        <p class="error-message">보고서 정보를 불러오는 데 실패했습니다.</p>
    </c:if>
     <div class="btn-group">
		<button class="btn btn-edit" onclick="loadPage('editInfo', '/nsg716/report/reporteditinfo?reportId=${reportInfo.reportId}', 'POST')">정보 수정</button>		
	</div>
</div>

</body>
</html>