<%@ tag language="java" pageEncoding="EUC-KR"%>

<%@ attribute name="name" required="true" %>
<%@ attribute name="email" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="status" required="true" %>

<div class="user-card" data-name="ȫ�浿" data-email="hong@example.com" data-role="������,������" data-id="1001">
    <img src="https://via.placeholder.com/150" alt="ȫ�浿 ����">
    <div class="user-info">
        <h3>${name}</h3>
        <p><strong>�̸���:</strong> ${email}</p>
        <p><strong>�ĺ���:</strong> ${id}</p>
        <p><strong>����:</strong> ${status}</p>
        <button onclick="alert('${name} �� ����!')">�� ����</button>
    </div>
</div>


<!-- �̰� �ƴϴ�. -->