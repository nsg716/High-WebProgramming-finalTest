<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>모델 정보 수정</title>
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
        <h1>모델 정보 수정</h1>
        <form action="/nsg716/model/updateinfo" method="post">
            <!-- 숨겨진 필드로 모델 ID 전달 -->
            <input type="hidden" name="modelId" value="${modelDTO.modelId}" />

            <div class="form-group">
                <label for="modelName">모델 이름</label>
                <input type="text" id="modelName" name="modelName" value="${modelDTO.modelName}" required />
                <c:if test="${not empty nameError}">
                    <span class="error-message">${nameError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="version">버전</label>
                <input type="text" id="version" name="version" value="${modelDTO.version}" required />
                <c:if test="${not empty versionError}">
                    <span class="error-message">${versionError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="modelType">모델 타입</label>
                <input type="text" id="modelType" name="modelType" value="${modelDTO.modelType}" required />
                <c:if test="${not empty typeError}">
                    <span class="error-message">${typeError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="framework">프레임워크</label>
                <input type="text" id="framework" name="framework" value="${modelDTO.framework}" required />
                <c:if test="${not empty frameworkError}">
                    <span class="error-message">${frameworkError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="description">설명</label>
                <textarea id="description" name="description" rows="4" required>${modelDTO.description}</textarea>
                <c:if test="${not empty descriptionError}">
                    <span class="error-message">${descriptionError}</span>
                </c:if>
            </div>

            <div class="form-group">
                <button type="submit" onclick="return confirm('정말 수정하시겠습니까?');">수정 완료</button>
            </div>
        </form>
    </div>
</body>
</html>
