<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 정보</title>
<style>
body, html {
    margin: 0;
    padding: 0;
    font-family: 'Arial', sans-serif;
    height: 100%;
    background-color: #f4f4f9;
}

.admin-info-container {
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

/* 모달 배경 스타일 */
#modal-background {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}

/* 모달 창 스타일 */
#modal {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: white;
    width: 400px;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
    z-index: 1000;
    text-align: center;
}

#modal-content {
    margin-bottom: 20px;
}

#modal-content h2 {
    margin-bottom: 10px;
}

#modal-content p {
    margin-bottom: 20px;
}

#modal-button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
}

#modal-button:hover {
    background-color: #0056b3;
}
</style>
</head>
<body>
<div class="admin-info-container">
    <h1>관리자 정보</h1>
    <c:if test="${not empty adminInfo}">
        <p>관리자 ID: ${adminInfo.id}</p>
        <p>이름: ${adminInfo.name}</p>
        <p>이메일: ${adminInfo.email}</p>
        <p>상태: ${adminInfo.status}</p>
        <c:if test="${not empty adminInfo.photo}">
            <img src="https://via.placeholder.com/150" alt="관리자 사진" />
        </c:if>
        <p>등록일: ${adminInfo.createDate}</p>
        <p>수정일: ${adminInfo.updateDate}</p>
		<p>최근 접속: ${adminInfo.lastLogin}</p>
    </c:if>
    <c:if test="${empty adminInfo}">
        <p class="error-message">관리자 정보를 불러오는 데 실패했습니다.</p>
    </c:if>
</div>
</body>
</html>