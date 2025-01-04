<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>

    <meta charset="UTF-8">
    <title>사용자 목록</title>
    <style>
        .filter-container {
            margin: 20px 0;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 8px;
            align-items: center; /* 필드와 버튼을 나란히 배치 */
        }

        .filter-input {
            width: 90%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            margin-right: 10px; /* 버튼과 간격 추가 */
        }


        .user-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            padding: 20px;
        }

        .user-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
            overflow: hidden;
        }

        .user-card:hover {
            transform: translateY(-5px);
        }

        .user-photo {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .user-info {
            padding: 15px;
        }

        .user-name {
            font-size: 18px;
            font-weight: bold;
            margin: 0 0 8px 0;
            color: #2c3e50;
        }

        .user-details {
            font-size: 14px;
            color: #666;
            margin: 4px 0;
        }

        .status-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            background-color: #e9ecef;
            text-align: center;
        }
        
         .user-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
            gap: 10px;
        }
        
         .user-search {
	        background-color: #007bff;
	        color: white;
	        border: none;
	        padding: 5px 10px;
	        border-radius: 5px;
	        cursor: pointer;
	        font-size: 12px;
	        margin-top: 10px;
	    }
	
	    .user-search:hover {
	        background-color: #0056b3;
	    }

        .action-button {
            flex-grow: 1;
            padding: 4px 8px;
            border: none;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .edit-button {
            background-color: #28a745;
            color: white;
        }
        .edit-button:hover {
            background-color: #218838;
        }

        .delete-button {
            background-color: #dc3545;
            color: white;
        }

        .delete-button:hover {
            background-color: #c82333;
        }
    </style>
</head>
 <script>

        function filterUsers() {
            const filterValue = document.getElementById('userFilter').value.toLowerCase();

            // 필터가 비어있는지 확인하고, 비어있다면 빈 객체 전달
            const params = filterValue
                ? {
                    'id': filterValue,
                    'name': filterValue,
                    'email': filterValue,
                    'status': filterValue,
                }
                : {}; // 빈 객체는 서버에서 전체 리스트를 반환하도록 처리

            userfilter(params);
        }

        // 필터링 요청 함수
        async function userfilter(params) {
            const mainContent = document.getElementById('refresh-user');

            try {
                const response = await fetch('user/userlist/filter', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(params),
                });

                if (!response.ok) {
                    const errorData = await response.text();
                    openModalFetch(errorData); // 오류 모달 표시
                } else {
                    const data = await response.text();
                    mainContent.innerHTML = data; // 성공 시 콘텐츠 업데이트
                }
            } catch (error) {
                console.error('Error during fetch:', error);
                openModalFetch('서버와의 연결에 문제가 발생했습니다.');
            }
        }
        
     // 특정 사용자 편집 페이지 로드 함수
        async function infoUser(userId) {
            const pageId = 'edit_user_${userId}';
            const pageUrl = '/nsg716/user/myinfo?userId='+ userId;
            await loadPage(pageId, pageUrl, 'GET', true); // reload = true로 설정
        }

        // 페이지 로드 함수
        async function loadPage(pageId, pageUrl, method = 'POST', reload = false) {
            const mainContent = document.querySelector('main');
            const existingPage = document.getElementById(pageId);

            // 이미 로드된 페이지 처리
            if (existingPage) {
                if (reload) {
                    existingPage.remove();
                } else {
                    setActivePage(pageId);
                    return;
                }
            }

            // 요청 메서드 전달 및 데이터 로드
            const response = await fetch(pageUrl, { method: method });
            const isOk = response.ok;
            const data = await response.text();

            if (!isOk) {
                openModalFetch(data); // 오류 발생 시 모달창 호출
            } else {
                const newPageCard = document.createElement('div');
                newPageCard.id = pageId;
                newPageCard.classList.add('page-card');
                newPageCard.innerHTML = data;
                mainContent.appendChild(newPageCard);

                setActivePage(pageId);
                adjustPaddingForHome(pageId);

                // 페이지 내 스크립트 재실행 처리
                const scripts = newPageCard.getElementsByTagName('script');
                Array.from(scripts).forEach((script, i) => {
                    const scriptId = `${pageId}_script_${i}`;
                    const existingScript = document.getElementById(scriptId);

                    if (existingScript) existingScript.remove();

                    const newScript = document.createElement('script');
                    newScript.id = scriptId;
                    newScript.text = script.text;
                    document.body.appendChild(newScript);
                });
            }
        }

        // 로컬에서 모달창 열기
        function openModal(message) {
            document.getElementById('modal-message').innerText = message;
            document.getElementById('modal').style.display = 'block';
            document.getElementById('modal-background').style.display = 'block';
        }

        // 모달창 닫기
        function closeModal() {
            document.getElementById('modal').style.display = 'none';
            document.getElementById('modal-background').style.display = 'none';
        }

        // 모달창 열기 (HTML 내용으로)
        function openModalFetch(html) {
            document.getElementById('modal-content').innerHTML = html;
            document.getElementById('modal').style.display = 'block';
            document.getElementById('modal-background').style.display = 'block';
        }

  

        function deleteUser(userId) {
            if (confirm('정말 삭제하시겠습니까?')) {
                // Send delete request
                fetch('/nsg716/user/deleteinfo?userId=' + userId, {
                    method: 'GET'
                })
                .then(response => {
                    if (response.ok) {
                        alert('사용자가 삭제되었습니다.');
                        filterUsers(); // 리스트를 갱신
                    } else {
                        alert('삭제 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('서버와의 연결에 문제가 발생했습니다.');
                });
            }
        }

 
    </script>
<body>
    <div class="filter-container">
        <input type="text"  class="filter-input"  id="userFilter" placeholder="이름, 이메일, 상태로 검색...">
        <button class="user-search" onclick="filterUsers()">검색</button>
    </div>
<div id="refresh-user">
    <div id="user-count" class="filter-container">
        총 사용자 수: <strong>${fn:length(users)}</strong>명
    </div>

    <div class="user-grid" id="userGrid">
        <c:forEach var="user" items="${users}">
            <div class="user-card" 
                 data-user-info="${user.name.toLowerCase()} ${user.email.toLowerCase()} ${user.status.toLowerCase()}">
                <img src="${empty user.photo ? 'https://via.placeholder.com/300' : user.photo}" 
                     alt="${user.name}" 
                     class="user-photo">
                <div class="user-info">
                    <h3 class="user-name">${user.name}</h3>
                    <p class="user-details">
                        <i class="fas fa-envelope"></i> ${user.email}
                    </p>
                    <p class="user-details">
                        <i class="fas fa-id-badge"></i> ${user.userId}
                    </p>
                    <div class="user-actions">
                       <span class="status-badge" 
                                  style="background-color: ${user.status eq 'active' ? '#28a745' : '#dc3545'}; width: 30%;">
                                ${user.status}
                       </span>
                       <button class="action-button edit-button" 
                               onclick="infoUser('${user.userId}')"> 보기      
                       </button>
                       <button class="action-button delete-button" 
                               onclick="deleteUser('${user.userId}')"> 삭제       
                       </button>
                        </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
   
</body>
</html>
