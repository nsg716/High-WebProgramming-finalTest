<%@ tag language="java" pageEncoding="EUC-KR"%>

<%@ attribute name="name" required="true" %>
<%@ attribute name="email" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="status" required="true" %>

<div class="user-card" data-name="홍길동" data-email="hong@example.com" data-role="관리자,개발자" data-id="1001">
    <img src="https://via.placeholder.com/150" alt="홍길동 사진">
    <div class="user-info">
        <h3>${name}</h3>
        <p><strong>이메일:</strong> ${email}</p>
        <p><strong>식별자:</strong> ${id}</p>
        <p><strong>상태:</strong> ${status}</p>
        <button onclick="alert('${name} 상세 정보!')">상세 보기</button>
    </div>
</div>


<!-- 이거 아니다. -->