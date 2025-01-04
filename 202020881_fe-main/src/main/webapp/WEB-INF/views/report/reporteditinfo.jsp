<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>보고서 정보 수정</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
        }

        .edit-form {
            max-width: 600px;
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

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 10px;
            color: #555;
        }

        .form-group input, .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .form-group input:focus, .form-group textarea:focus {
            border-color: #007bff;
            outline: none;
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
    <div class="edit-form">
        <h1>보고서 정보 수정</h1>
        <form action="/nsg716/report/updateinfo" method="post">
            <!-- 숨겨진 필드로 보고서 ID 전달 -->
            <input type="hidden" name="reportId" value="${reportDTO.reportId}" />
			<input type="hidden" name="model" value="${reportDTO.model}" />
            <input type="hidden" name="admin" value="${reportDTO.admin}" />
            <input type="hidden" name="user" value="${reportDTO.user}" />
            
            
            <div class="form-group">
                <label for="reportName">보고서 이름</label>
                <input type="text" id="reportName" name="reportName" value="${reportDTO.reportName}" required />
                <c:if test="${not empty reportNameError}">
                    <span class="error-message">${reportNameError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="modelName">모델 이름</label>
                <input type="text" id="modelName" name="modelName" value="${reportDTO.modelName}" required />
                <c:if test="${not empty modelNameError}">
                    <span class="error-message">${modelNameError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="usageDate">사용 일시</label>
                <input type="datetime-local" id="usageDate" name="usageDate" value="${reportDTO.usageDate}" required />
                <c:if test="${not empty usageDateError}">
                    <span class="error-message">${usageDateError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="accuracy">정확도 (%)</label>
                <input type="number" id="accuracy" name="accuracy" value="${reportDTO.accuracy}" step="0.01" required />
                <c:if test="${not empty accuracyError}">
                    <span class="error-message">${accuracyError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="totalProcessingAmount">총 처리 양</label>
                <input type="number" id="totalProcessingAmount" name="totalProcessingAmount" value="${reportDTO.totalProcessingAmount}" required />
                <c:if test="${not empty totalProcessingAmountError}">
                    <span class="error-message">${totalProcessingAmountError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="averageResponseTime">평균 응답 시간 (s)</label>
                <input type="number" id="averageResponseTime" name="averageResponseTime" value="${reportDTO.averageResponseTime}" step="0.01" required />
                <c:if test="${not empty averageResponseTimeError}">
                    <span class="error-message">${averageResponseTimeError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <button type="submit" onclick="return confirm('정말 수정하시겠습니까?');">수정 완료</button>
            </div>
        </form>
    </div>
</body>
</html>
