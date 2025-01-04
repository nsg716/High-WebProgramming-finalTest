<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>

    <meta charset="UTF-8">
    <title>모델 목록</title>
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

        .model-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            padding: 20px;
        }

        .model-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
            overflow: hidden;
        }

        .model-card:hover {
            transform: translateY(-5px);
        }

        .model-info {
            padding: 15px;
        }

        .model-name {
            font-size: 18px;
            font-weight: bold;
            margin: 0 0 8px 0;
            color: #2c3e50;
        }

        .model-details {
            font-size: 14px;
            color: #666;
            margin: 4px 0;
        }

        .model-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
            gap: 10px;
        }
        
        .model-search {
	        background-color: #007bff;
	        color: white;
	        border: none;
	        padding: 5px 10px;
	        border-radius: 5px;
	        cursor: pointer;
	        font-size: 12px;
	        margin-top: 10px;
	    }
	
	    .model-search:hover {
	        background-color: #0056b3;
	    }

        .action-button {
            flex-grow: 1;
            padding: 8px;
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

        function filterModels() {
            const filterValue = document.getElementById('modelFilter').value.toLowerCase();
            const params = filterValue
                ? { 
            		'modelName': filterValue, 
            		'version': filterValue, 
            		'modelType': filterValue,
            		'framework' : filterValue}  
                : {};
            modelfilter(params);
            

        }

        async function modelfilter(params) {
            const mainContent = document.getElementById('refresh-model');

            try {
                const response = await fetch('model/modellist/filter', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(params),
                });

                if (response.ok) {
                    const data = await response.text();
                    mainContent.innerHTML = data;
                } else {
                    alert('필터링 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('서버와의 연결에 문제가 발생했습니다.');
            }
        }


        function deleteModel(modelId) {
            if (confirm('정말 삭제하시겠습니까?')) {
            	
                fetch('/nsg716/model/delete?modelId=' + modelId, {
                	method: 'GET' 
                	})
                    .then(response => {
                        if (response.ok) {
                            alert('모델이 삭제되었습니다.');
                            filterModels();
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
        
        function reportcreate(modelId) {
            if (confirm('리포트를 작성합니까?')) {
            	 window.location.href = '/nsg716/report/reportcreate?modelId=' + modelId;
        	}
        }

     
    </script>
</head>
<body>
    <div class="filter-container">
        <input type="text" class="filter-input" id="modelFilter" placeholder="모델 이름, 버전, 유형, 프레임워크로 검색...">
    	<button class="model-search" onclick="filterModels()">검색</button>
    </div>
    <div id="refresh-model">
        <div id="model-count" class="filter-container">
            총 모델 수: <strong>${fn:length(models)}</strong>개
        </div>

        <div class="model-grid" id="modelGrid">
            <c:forEach var="model" items="${models}">
                <div class="model-card" 
                     data-model-info="${model.modelName.toLowerCase()} ${model.version.toLowerCase()} ${model.modelType.toLowerCase()}">
                    <div class="model-info">
                        
                        <h3 class="model-name">${model.modelName}</h3>
                        <p class="model-details">
                            <strong>버전:</strong> ${model.version}
                        </p>
                        <p class="model-details">
                            <strong>유형:</strong> ${model.modelType}
                        </p>
                        <p class="model-details">
                            <strong>프레임워크:</strong> ${model.framework}
                        </p>
            	
            
                        <div class="model-actions">
                            <c:if test="${not empty sessionScope.admin}">
                            	<button 
								    class="action-button edit-button" 
								    onclick="loadPage('modelinfo', '/nsg716/model/info?modelId=${model.modelId}', 'GET')">
								    정보 보기
								</button>
                            	<button class="action-button delete-button" onclick="deleteModel('${model.modelId}')">삭제</button>
                            </c:if>
                           	<c:if test="${not empty sessionScope.user}">
                           		<button class="action-button edit-button" onclick="reportcreate('${model.modelId}')">리포트 작성</button>
                           	</c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>


</body>
</html>
