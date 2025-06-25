<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>로그인</title>
</head>
<body>
    <%@include file="/WEB-INF/views/common/header.jsp" %>
    
    <div>
        <h3>로그인</h3>
        <form action="member/login" id="loginForm" method="post" novalidate>
            <!-- 아이디 -->
            <div>
                <label>아이디</label><br>
                <input type="text" id="userId" placeholder="아이디" name="userId" required>
            </div>

            <!-- 비밀번호 -->
            <div>
                <label for="userPwd">비밀번호</label><br>
                <input type="password" id="userPwd" placeholder="비밀번호" name="userPwd" required>
            </div>

            <!-- 제출 -->
            <div>
                <button type="submit" id="loginFormSubmit" disabled>
                    로그인
                </button>
            </div>
            
            <div id="submitResult"></div>
        </form>
    </div>

    <script type="text/javascript">
    // 검증 상태 변수
    let idPass = false;
    let pwdPass = false;

    // 아이디 검증
    document.getElementById("userId").addEventListener('input', function(){
        idConfirm();
    });

    // 아이디 확인 함수
    function idConfirm(){
        const userId = document.getElementById("userId").value.trim();
		const regex = /^[A-Za-z0-9]{1,30}$/;
        
        if (regex.test(userId)) {
            idPass = true;
        } else {
            idPass = false;
        }
        toggleSubmit();
    }

    // 비밀번호 검증
    document.getElementById('userPwd').addEventListener('input', function() {
        const pwd = this.value.trim();
	    const regex = /^[A-Za-z0-9]{4,20}$/;
        pwdPass = regex.test(pwd);
        toggleSubmit();
    });

    // 제출 버튼 상태 관리
    function toggleSubmit(){
        let submitBtn = document.getElementById("loginFormSubmit");
        let messageSpan = document.getElementById("validation-message");
        let userId = document.getElementById("userId").value;
        let userPwd = document.getElementById("userPwd").value;
        
        if(idPass && pwdPass) {
            submitBtn.disabled = false;
            messageSpan.textContent = 'DORO!';
            return;
        } else if(idPass) {
            if(userPwd === ""){
                messageSpan.textContent = "비밀번호를 입력해주세요";
            }else{
                messageSpan.textContent = "비밀번호는 특수문자 제외 4~30 글자입니다.";
            }
        } else if(pwdPass){
            if(userId === ""){
                messageSpan.textContent = "아이디를 입력해주세요";
            }else{
                messageSpan.textContent = "아이디는 특수문자 제외 1~30 글자입니다.";
            }
        } else{
            messageSpan.textContent = "아이디와 비밀번호를 입력하세요";
        }
        submitBtn.disabled = true;
    };

    //제출 버튼 이벤트 핸들러
    document.getElementById("loginForm").addEventListener("submit", function(event) {
        event.preventDefault();
        
        let submitBtn = document.getElementById('loginFormSubmit');
        let submitResult = document.getElementById('submitResult');
        submitBtn.disabled = true;
        submitBtn.innerHTML = `처리 중...`;
        
		let userId = document.getElementById("userId").value.trim();
		let userPwd = document.getElementById("userPwd").value.trim();
		$.ajax({
			type: "POST",
			url: "member/login",
			data: {userId, userPwd},
			success: function(result) {
				if(result == "pass"){
					window.location.href = "aiWeather";
				}else{
			        submitBtn.innerHTML = `로그인`;
			        submitResult.innerHTML = `아이디 혹은 비밀번호가 일치하지 않습니다.`;
				}
		    },
		    error: function(){
		    }
      	});
    });
    </script>

</body>
</html>
