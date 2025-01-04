<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<div class="home-container">
    <div class="overlay"></div>
    <div class="home-content">
        <p>
            우리는 혁신적인 IT 및 AI 기술로 미래를 선도합니다. 최고의 기술력과 창의력을 바탕으로 
            고객에게 최적의 솔루션을 제공합니다. 우리는 끊임없는 도전과 발전을 통해 글로벌 
            시장에서 기술 리더로 자리매김하고 있습니다.
        </p>
        <p>
            우리 회사는 인공지능, 빅데이터 분석, 클라우드 컴퓨팅, IoT 등 최신 기술을 접목하여
            다양한 산업 분야에서 고객의 가치를 극대화할 수 있는 서비스를 제공하고 있습니다.
        </p>
        <p>
            미래를 함께 열어갈 파트너로서, 우리는 새로운 가능성을 탐구하고 기술 혁신을 통해
            더 나은 세상을 만들어가고 있습니다.
        </p>
    </div>
</div>

<style>
    body, html {
        margin: 0;
        padding: 0;
        font-family: 'Arial', sans-serif;
        height: 100%;
    }
    
    main {
    	padding: 0px;
    }
    
    .home-container {
        position: relative;
        background-image: url('/nsg716/image/home-background.jpg');
        background-size: cover;
        background-position: center;
        height: 100vh; /* 화면 높이 전체를 채움 */
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        padding: 20px;
        filter: brightness(1.7);
    }

    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5); /* 어두운 오버레이 */
    }

    .home-content {
        position: relative;
        z-index: 1;
        max-width: 800px;
        padding: 20px;
        background-color: rgba(0, 0, 0, 0.6);
        border-radius: 10px;
    }

    p {
        font-size: 18px;
        line-height: 1.6;
         margin: 10px 0;
    	padding: 10px;
        border-bottom: 1px solid #eee;
    }
    

    /* 반응형 디자인 */
    @media (max-width: 768px) {
        p {
            font-size: 16px;
        }
    }

    @media (max-width: 480px) {
        p {
            font-size: 14px;
        }
    }
</style>
