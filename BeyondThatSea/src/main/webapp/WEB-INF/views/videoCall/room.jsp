<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://unpkg.com/@openvidu/webcomponents@2.23.0/dist/openvidu-webcomponent/openvidu-webcomponent.js"></script>
<c:set var="vcRoom" value="${sessionScope.videoCallRoom}"/>
<meta charset="UTF-8">
<title>${vcRoom.UserName}님의 방</title>
</head>
<body>
    <%@include file="/WEB-INF/views/common/header.jsp" %>
    
    <!-- 화상 회의 컨테이너 -->
    <div style="width: 100%; height: 80vh;">
        <!-- Web Component: 세션 ID와 토큰을 동적으로 설정 -->
        <openvidu-webcomponent 
            id="ov-webcomponent" 
            session-id="${vcRoom.vcSession}">
        </openvidu-webcomponent>
    </div>

    <script>
    <!-- 토큰 처리 -->
    document.addEventListener('DOMContentLoaded', async () => {
        const webComponent = document.getElementById('ov-webcomponent');
        
        //토큰 요청 함수
        const getToken = async (sessionId) => {
            const response = await fetch(`${root}/videoCall/participate/\${sessionId}`, {
                method: 'POST'
            });
            return await response.json();
        };

        try {
            // 서버에서 토큰 생성 요청
            const tokenData = await getToken('${vcRoom.vcSession}');
            
            // Web Component에 토큰 설정
            webComponent.token = tokenData.token;
            
            // 연결 성공 이벤트 핸들러
            webComponent.addEventListener('onSessionCreated', (event) => {
                console.log("세션 연결 성공!", event.detail);
            });
        } catch (error) {
            console.error("토큰 오류:", error);
            alert("화상 회의 접속 실패!");
        }
    });
    </script>
</body>
</html>
