<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>모델 데이터 생성</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
            justify-content: center;
            align-items: center;
            display : flex;
        }
        .model-form {
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 400px;
        }
        .model-form h1 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            margin-bottom: 10px;
            color: #555;
        }
        .form-group input, .form-group textarea {
            display: block;
            width: 100%;
     		padding: 10px 0; /* 상하 10픽셀, 좌우 0픽셀 */
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-group input:focus, .form-group textarea:focus {
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
    <form action="/nsg716/modelcreate" method="post" class="model-form">
        <h1>모델 데이터 생성</h1>
        <div class="form-group">
            <label for="model_id">모델 ID</label>
            <input type="text" id="modelId" name="modelId" value="${modelDTO.modelId}" required>
            
            <c:if test="${not empty modelIdError}">
                <span class="error-message">${modelIdError}</span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="model_name">모델 이름</label>
            <input type="text" id="modelName" name="modelName"  value="${modelDTO.modelName}" required>
        </div>
        <div class="form-group">
            <label for="version">버전</label>
            <input type="text" id="version" name="version" value="${modelDTO.version}"  required>
        </div>
        <div class="form-group">
            <label for="model_type">모델 타입</label>
            <input type="text" id="modelType" name="modelType"  value="${modelDTO.modelType}" required>
        </div>
        <div class="form-group">
            <label for="framework">프레임워크</label>
            <input type="text" id="framework" name="framework" value="${modelDTO.framework}" required>
        </div>
        <div class="form-group">
            <label for="description">설명</label>
            <textarea id="description" name="description" rows="4"  value="${modelDTO.description}"  ></textarea>
        </div>
        <div class="form-group">
            <button type="submit">생성하기</button>
        </div>
    </form>
</body>
</html>
