<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Webapp nsg716</title>

    <style>
 
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}
		
		body {
			font-family: 'Arial', sans-serif;
			display: flex;
			flex-direction: column;
			min-height: 100vh;
		}
		
		header {
			background-color: #007bff;
			color: white;
			padding: 20px;
			font-size: 24px;
			position: sticky;
			top: 0;
			display: flex;
			align-items: center;
			z-index: 1001;
		}
		
		header button {
			background-color: white;
			border: none;
			padding: 10px;
			border-radius: 5px;
			cursor: pointer;
			font-size: 18px;
			color: #007bff;
			margin-right: 10px;
		}
		
		header button:hover {
			background-color: #0056b3;
			color: white;
		}
		
		.layout {
			display: flex;
			flex: 1;
		}
		
		.sidebar-container {
			width: 250px;
			background-color: #343a40;
			color: white;
			padding: 20px;
			display: flex;
			flex-direction: column;
			height: 100%;
		}
		
		.sidebar-container.hidden {
			width: 0;
			padding: 0;
			overflow: hidden;
		}
		
		nav ul {
			list-style: none;
			padding-left: 0;
		}
		
		nav ul li {
			margin-bottom: 15px;
		}
		
		nav ul li a {
			color: white;
			text-decoration: none;
			font-size: 18px;
			display: block;
		}
		
		nav ul li a:hover {
			color: #007bff;
		}
		
		.submenu {
			margin-left: 20px;
			display: none;
		}
		
		.submenu.active {
			margin-top: 15px;
			display: block;
		}
		
		main {
			flex: 1;
			padding: 20px;
			position: relative;
			justify-content: center;
            align-items: center;
			overflow-y: auto; /* 메인 콘텐츠에서 스크롤이 가능하게 설정 */
		}
		
		.page-card {
			display: none;
			width: 100%;
			min-height: 100%; /* 페이지 카드의 높이를 부모 요소에 맞추어 설정 */
			padding: 20px;
			background-color: white;
			overflow-y: auto; /* 페이지 카드 내에서 스크롤 가능 */
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		}
		
		.page-card-home {
			display: none;
			width: 100%;
			min-height: 100%; /* 페이지 카드의 높이를 부모 요소에 맞추어 설정 */
			/* padding: 20px; */
			background-color: white;
			overflow-y: auto; /* 페이지 카드 내에서 스크롤 가능 */
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		}
		
		.page-card.active {
			display: block;
		}
		
		footer {
			background-color: #007bff;
			color: white;
			text-align: center;
			padding: 10px;
			margin-top: auto;
			width: 100%;
		}
		
		@media ( max-width : 768px) {
			.sidebar-container {
				position: relative;
				width: 100%;
				height: auto;
				padding: 20px;
				overflow: hidden;
				transition: height 0.3s ease;
				z-index: 1000;
			}
			.sidebar-container.hidden {
				display: none;
			}
			.layout {
				flex-direction: column;
			}
			main {
				padding: 20px;
			}
		}
		  /* 4주차 내용 작성  */
		#auth-section {
			display: flex;
			align-items: center;
		}
		
		#auth-section button {
			background-color: white;
			border: none;
			padding: 10px;
			border-radius: 5px;
			cursor: pointer;
			font-size: 18px;
			color: #007bff;
			margin-left: 10px;
		}
		
		#auth-section button:hover {
			background-color: #0056b3;
			color: white;
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
		
		/* 모달의 content div 스타일 */
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
    
    
    
   
    <script>
    	/* 7주차 강의 내용 */

	    
	    async function loadPage(pageId, pageUrl, method = 'POST', reload=false) {
		    const mainContent = document.querySelector('main');
		    const existingPage = document.getElementById(pageId);
		
		    // 이미 로드된 페이지가 있으면 reload 조건에 따라 처리
		    if (existingPage) {
		        if (reload) {
		            existingPage.remove();
		        } else {
		            setActivePage(pageId);
		            return;
		        }
		    }
		
		    // 요청 메서드를 파라미터로 전달
		    const response = await fetch(pageUrl, {
		        method: method
		    });
		
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
		
		        // 페이지 내 script 태그 재실행 처리
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
	
        
     // padding을 home 페이지일 때만 0으로 설정하는 함수
        function adjustPaddingForHome(pageId) {
            const pageElement = document.getElementById(pageId);
            
            if (pageId === 'home') {
                pageElement.style.padding = '0px';  // home 페이지의 padding을 0으로 설정
            } else {
                pageElement.style.padding = '20px';  // 다른 페이지의 padding을 기본값으로 설정
            }
        }

        // 특정 페이지를 활성화하는 함수
        function setActivePage(pageId) {
            const pages = document.querySelectorAll('.page-card');
            pages.forEach(page => {
                page.classList.remove('active');
            });

            const targetPage = document.getElementById(pageId);
            if (targetPage) {
                targetPage.classList.add('active');
                localStorage.setItem('currentPage', pageId); // 현재 페이지 상태 저장
            }
        }

        // 서브메뉴 토글 함수
        function UsertoggleSubmenu() {
            const submenu = document.getElementById('Usersubmenu');
            submenu.classList.toggle('active');
        }
        
        function ModeltoggleSubmenu() {
            const submenu = document.getElementById('Modelsubmenu');
            submenu.classList.toggle('active');
        }
        
        function ReporttoggleSubmenu() {
            const submenu = document.getElementById('Reportsubmenu');
            submenu.classList.toggle('active');
        }

        // 메뉴 접기/펼치기 버튼 동작
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar-container');
            sidebar.classList.toggle('hidden');
            sidebar.classList.toggle('active');
        }
		
        // 시작 화면 세팅
        document.addEventListener('DOMContentLoaded', function () {
            loadPage('home', '/nsg716/home');

        });
    	
        // 로그인 윈도우 표시하는 함수
        function showLogin() {
            window.location.href = '/nsg716/login';
        }
	
        // 로그아웃 처리 함수
        function logout() {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'logout'; // 로그아웃을 처리하는 서버의 URL

            document.body.appendChild(form); // 폼을 문서에 추가
            form.submit(); // 폼 제출
        }
        
        function showSignup() {
            // 회원가입 페이지로 이동
            window.location.href = '/nsg716/signup';
        }
        
        function showModelcreate() {
            // 회원가입 페이지로 이동
            window.location.href = '/nsg716/model/modelcreate';
        }
    </script>

<script type="text/javascript" src="/nsg716/js/common.js"></script>
</head>
<body>


	<header>
	    <button onclick="toggleSidebar()">☰</button>
	    <span>Webapp nsg716</span>
	    <div id="auth-section" style="margin-left: auto;">
  
	        
	        
	        <c:if test="${empty sessionScope.user && empty sessionScope.admin}">
	            <button id="login-button" onclick="showLogin()"> 로그인 </button>
	        </c:if>
	        
	        <c:if test="${not empty sessionScope.user || not empty sessionScope.admin}">
	            <div id="user-info" style="display: ${not empty sessionScope.user || not empty sessionScope.admin ? 'block' : 'none'};">
	            	<c:if test="${not empty sessionScope.user}">
	                	<span id="username" title="${sessionScope.user.email}">${sessionScope.user.name}</span>
	                </c:if>
	                <c:if test="${not empty sessionScope.admin}">
	                	<span id="username" title="${sessionScope.admin.email}">${sessionScope.admin.name}</span>
	                </c:if>
	                <button id="logout-button" onclick="logout()"> 로그아웃 </button>
	                
	            </div>
	        </c:if>

	         
	        <c:if test="${empty sessionScope.user && empty sessionScope.admin}">
	            <button id="login-button" onclick="showSignup()"> 회원가입 </button>
	        </c:if>
	    </div>
	</header>
	
	<div id="modal-background"></div>
	
	<!-- 모달 창 -->
	<div id="modal">
	    <div id="modal-content">
	        <h2 id="modal-title">알림</h2>
	        <p id="modal-message">이것은 모달 팝업 메시지입니다.</p>
	    </div>
	    <button onclick="closeModal()">닫기</button>
	</div>
	


	
    <div class="layout">
       <div class="sidebar-container hidden">
        <nav>
		    <ul>
		        <li><a href="#" onclick="loadPage('home', '/nsg716/home')" >홈</a></li>
		     	<c:if test="${not empty sessionScope.user}">
		     		<li><a href="#" onclick="loadPage('myinfo', '/nsg716/user/myinfo','GET')">내정보</a></li>
		     	</c:if>
		     	
		        <c:if test="${not empty sessionScope.admin}">
		         	<li><a href="#" onclick="loadPage('admininfo', '/nsg716/admin/admininfo','GET')">관리자 정보</a></li>

		     		<li><a href="#" onclick="UsertoggleSubmenu()">사용자 관리</a>
		                <ul id="Usersubmenu" class="submenu">
		                	<!-- 링크 수정 -->
		                    <li><a href="signup" onclick="showSignup()">사용자 생성</a></li>
		                    <li><a href="#" onclick="loadPage('userlist', '/nsg716/user/userlist','GET')">사용자 조회 / 삭제</a></li>
		                </ul>
		            </li>
					</c:if>
					<c:if test="${not empty sessionScope.admin}">
						<li><a href="#"onclick="ModeltoggleSubmenu()">모델 관리</a>
							<ul id="Modelsubmenu" class="submenu">
								<li><a href="modelcreate" onclick="showModelcreate()">모델 생성</a></li>
								<li><a href="#" onclick="loadPage('modellist', '/nsg716/model/modellist','GET')">모델 조회 / 삭제</a></li>
							</ul>
						</li>
					</c:if>
					<c:if test="${not empty sessionScope.user}">
						<li><a href="#"onclick="ModeltoggleSubmenu()">모델 / 리포트 관리</a>
							<ul id="Modelsubmenu" class="submenu">
								<li><a href="#" onclick="loadPage('modellist', '/nsg716/model/modellist','GET')">모델 조회 / 리포트 작성</a></li>
							</ul>
						</li>
					</c:if>
			          	    

				<!-- 로그인 시 사용 가능 기능 테스트 구역 -->
				<c:if test="${not empty sessionScope.user}">
		        <li><a href="#"onclick="ReporttoggleSubmenu()">모델 리포트</a>
		          <ul id="Reportsubmenu" class="submenu">
		                	<!-- 링크 수정 -->
		                    <li><a href="#" onclick="loadPage('reportlist', '/nsg716/report/reportlist','GET')">리포트 조회</a></li>
		                </ul>      
		        </li>
		        </c:if>
		        <c:if test="${not empty sessionScope.admin}">
		        <li><a href="#"onclick="ReporttoggleSubmenu()">모델 리포트</a>
		          <ul id="Reportsubmenu" class="submenu">
		                	<!-- 링크 수정 -->
		                    <li><a href="#" onclick="loadPage('reportlist', '/nsg716/report/reportlist','GET')">리포트 조회 / 삭제</a></li>
		                </ul>      
		        </li>
		        </c:if>
		        
		        

		    </ul>
		</nav>
        </div>

        <main>

        </main>
    </div>

    <footer>
        © 2024 내 웹사이트 - 모든 권리 보유
    </footer>

</body>
</html>
