<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Report"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>

    <meta charset="UTF-8">
    <title>리포트 목록</title>
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
        }


        .report-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            padding: 20px;
        }

        .report-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
            overflow: hidden;
        }

        .report-card:hover {
            transform: translateY(-5px);
        }

        .report-info {
            padding: 15px;
        }

        .report-name {
            font-size: 18px;
            font-weight: bold;
            margin: 0 0 8px 0;
            color: #2c3e50;
        }

        .report-details {
            font-size: 14px;
            color: #666;
            margin: 4px 0;
        }

        .report-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
            gap: 10px;
        }
  		
  		.report-search {
	        background-color: #007bff;
	        color: white;
	        border: none;
	        padding: 5px 10px;
	        border-radius: 5px;
	        cursor: pointer;
	        font-size: 12px;
	        margin-top: 10px;
	    }
	
	    .report-search:hover {
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

        .view-button {
            background-color: #007bff;
            color: white;
        }

        .view-button:hover {
            background-color: #0056b3;
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

        
        function filterReports() {
            const filterValue = document.getElementById('reportFilter').value.toLowerCase();
            const params = filterValue
                ? {
                    'reportName': filterValue,
                    'modelName': filterValue
                }
                : {};
                reportfilter(params);
        }

        async function reportfilter(params) {
            const mainContent = document.getElementById('refresh-report');

            try {
                const response = await fetch('report/reportlist/filter', {
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

        function deleteReport(reportId) {
            if (confirm('정말 삭제하시겠습니까?')) {
                fetch('/nsg716/report/delete?reportId=' + reportId, {
                    method: 'GET'
                })
                    .then(response => {
                        if (response.ok) {
                            alert('리포트가 삭제되었습니다.');
                            filterReports();
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
        <input type="text" class="filter-input" id="reportFilter" placeholder="리포트 이름, 모델 이름으로 검색...">
        <button class="report-search" onclick="filterReports()">검색</button>
    </div>
    <div id="refresh-report">
        <div id="report-count" class="filter-container">
            총 리포트 수: <strong>${fn:length(reports)}</strong>개
        </div>

        <div class="report-grid" id="reportGrid">
            <c:forEach var="report" items="${reports}">
                <div class="report-card" 
                     data-report-info="${report.reportName.toLowerCase()} ${report.modelName.toLowerCase()}">
                    <div class="report-info">
                        <h3 class="report-name">${report.reportName}</h3>
                        <p class="report-details">
                            <strong>모델 이름:</strong> ${report.modelName}
                        </p>
                        <p class="report-details">
                            <strong>정확도:</strong> ${report.accuracy}%
                        </p>
                        <p class="report-details">
                            <strong>평균 응답 시간:</strong> ${report.averageResponseTime}초
                        </p>
                        <p class="report-details">
                            <strong>작성 날짜:</strong> ${report.usageDate}
                        </p>

                        <div class="report-actions">
                            <c:if test="${not empty sessionScope.admin or not empty sessionScope.user}">
                                <button class="action-button view-button" 
                                    onclick="loadPage('reportinfo', '/nsg716/report/info?reportId=${report.reportId}', 'GET')">
                                    자세히 보기
                                </button>
                                <c:if test="${not empty sessionScope.admin}">
                                    <button class="action-button delete-button" 
                                        onclick="deleteReport('${report.reportId}')">
                                        삭제
                                    </button>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>

</html>