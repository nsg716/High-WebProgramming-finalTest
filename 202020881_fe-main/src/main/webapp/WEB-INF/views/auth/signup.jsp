<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <style>
        body, html {
		    margin: 0;
		    padding: 0;
		    font-family: 'Arial', sans-serif;
		    height: 100%;
		    background-color: #f4f4f9;
		   	justify-content: center;
            align-items: center;
            display: flex;
		}
		        
        .register-form {
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 400px;
    
        }
        .register-form h1 {
            text-align: center;
            color: #333;
        }
        .form-group {
        	display: block;
            margin-bottom: 20px;
        }
        .form-group label {
            margin-bottom: 10px;
            color: #555;
        }
		.form-group input {
		    display: block;
		    width: 100%;
		    padding-top: 10px;
		    padding-bottom: 10px;
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
		    display: block;
		  }

		}
		
		</style>
</head>




<body>


	<form action="/nsg716/signup" method="post" class="register-form">
	    <h1>회원가입</h1>
	    <div class="form-group">
	        <label for="id">아이디</label>
	        <input type="text" id="id" name="id" value="${userDTO.id}" required>
	        <c:if test="${not empty idError}">
	            <span class="error-message">${idError}</span>
	        </c:if>
	    </div>
	    <div class="form-group">
	        <label for="name">이름</label>
	        <input type="text" id="name" name="name" value="${userDTO.name}" required>
	        <c:if test="${not empty nameError}">
	            <span class="error-message">${nameError}</span>
	        </c:if>
	    </div>
	    <div class="form-group">
	        <label for="email">이메일</label>
	        <input type="email" id="email" name="email" value="${userDTO.email}" required>
	        <c:if test="${not empty emailError}">
	            <span class="error-message">${emailError}</span>
	        </c:if>
	    </div>
	    <div class="form-group">
	        <label for="password">비밀번호</label>
	        <input type="password" id="password" name="password" required>
	        <c:if test="${not empty passwordError}">
	            <span class="error-message">${passwordError}</span>
	        </c:if>
	    </div>
	    <div class="form-group">
	        <c:if test="${not empty errorMessage}">
	            <span class="error-message">${errorMessage}</span>
	        </c:if>
	        <button type="submit">회원가입</button>
	    </div>
	</form>


</body>

</html>
