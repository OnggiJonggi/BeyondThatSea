<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>저 바다 너머</title>
</head>
<body>
	<%@include file="/WEB-INF/views/common/header.jsp"%>

	첫 시안은 YAMI가 되었습니다. 숭고한 희생...
	<br>

	<div id="serverIpDiv">
		<button id="getIpBtn">서버 내부 IP 가져오기</button>
	</div>

	<hr>
	perplexity 써먹기
	<form action="${root}/ai/search" method="post">
		입력해요 : <input type="text" name="content" required /> <br />
		<button type="submit">DORO!!</button>
	</form>


	<hr>
	<c:choose>
		<c:when test="${empty loginUser}">
			<a href="${root}/member/enroll">회원가입</a>
			
			<br>
			<form action="${root}/member/login" method="post">
				아이디 : <input type="text" name="userId" required /> <br />
				비밀번호 : <input type="password" name="userPwd" required />
				<button type="submit" id="loginFormSubmit">DORO!!</button>
			</form>
		</c:when>
		<c:otherwise>
			${loginUser.userName}님 딱히 환영하진 않습니다.
		</c:otherwise>
	</c:choose>


	<hr>
	<a href="${root}/videoCall/vcMain">영상통화</a>
	<hr>
	<a href="${root}/screenShare/ssMain">화면공유</a>


<script type="text/javascript">
$(document).ready(function(){
	
	//내부 ip주소 알아보기
    $('#getIpBtn').click(function(){
        $.ajax({
            url : "${root}/findServerPrivateIP",
            method : "post",
            data : {},
            success : function(result){
                $('#serverIpDiv').text('서버 내부 ip주소 : ' + result);
            },
            error : function(){
                console.log('통신 실패');
            }
        });
    });
	
	
	//로그인
    $('#loginFormSubmit').click(function(e){
    	e.preventDefault();
        var userId = $('input[name="userId"]').val();
        var userPwd = $('input[name="userPwd"]').val();
        $.ajax({
        	url: "${root}/member/login",
            method : "post",
            data : {userId, userPwd},
            success : function(result){
            	location.reload();
            },
            error : function(){
                console.log('통신 실패');
            }
        });
    });
	
});

</script>

</body>
</html>
