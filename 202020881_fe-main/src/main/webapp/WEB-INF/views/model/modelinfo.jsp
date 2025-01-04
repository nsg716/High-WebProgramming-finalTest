<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>모델 정보</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            height: 100%;
            background-color: #f4f4f9;
        }
        
        .model-info-container {
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
        
  
        
        .btn:hover {
            opacity: 0.9;
        }
        
    </style>
</head>

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
    <div class="model-info-container">
        <h1>모델 정보</h1>
        <c:if test="${not empty modelInfo}">
            <p>모델 ID: ${modelInfo.modelId}</p>
            <p>관리자 ID: ${modelInfo.adminId}</p>
            <p>모델 이름: ${modelInfo.modelName}</p>
            <p>버전: ${modelInfo.version}</p>
            <p>모델 타입: ${modelInfo.modelType}</p>
            <p>프레임워크: ${modelInfo.framework}</p>
            <p>설명: ${modelInfo.description}</p>
            <p>생성일: ${modelInfo.createDate}</p>
            <p>수정일: ${modelInfo.updateDate}</p>
         
        </c:if>
        
        <c:if test="${empty modelInfo}">
            <p class="error-message">모델 정보를 불러오는 데 실패했습니다.</p>
        </c:if>
        <div class="btn-group">
           	<c:if test="${not empty sessionScope.admin}">
				    <button class="btn btn-edit" onclick="loadPage('editInfo', '/nsg716/model/modeleditinfo?modelId=${modelInfo.modelId}', 'POST')">정보 수정</button>		
    	    </c:if>
		</div>
    </div>
</body>
</html>