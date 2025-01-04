<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>내 정보</title>
</head>
    <style>
		 body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
        }
        
        .user-info-container {
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
    
    async function loadPage(pageId, pageUrl, method = 'POST', reload = false) {
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

        // POST 요청을 보내면서 파라미터를 전달
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

    
    </script>
<body>
	 <div class="user-info-container">
        <h1>유저 정보</h1>
		    <c:if test="${not empty userInfo}">
		        <p>사용자 ID: ${userInfo.userId}</p>
		        <p>이름: ${userInfo.name}</p>
		        <p>이메일: ${userInfo.email}</p>
		        <p>비밀번호: ${userInfo.password}</p>
		        <p>상태: ${userInfo.status}</p>
		        <c:if test="${not empty userInfo.photo}">
		            <img src="https://via.placeholder.com./150" alt="사용자 사진" />
		        </c:if>
		        <p>가입일: ${userInfo.createDate}</p>
		        <p>수정일: ${userInfo.updateDate}</p>
		   
	            <div class="btn-group">
	             	<c:if test="${not empty sessionScope.user}">
				    <button class="btn btn-edit" onclick="loadPage('editInfo', '/nsg716/user/editinfo', 'POST')">정보 수정</button>				    
				    <button class="btn btn-delete" onclick="if(confirm('변경하시겠습니까?')) { location.href = '/nsg716/user/deleteinfo'; }">상태 변경</button>
				    </c:if>
				</div>
        	</c:if>
        <c:if test="${empty userInfo}">
            <p class="error-message">정보를 불러오는 데 실패했습니다.</p>
        </c:if>
    </div>
    

    
</body>