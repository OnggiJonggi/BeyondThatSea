<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8"/>
	<title>aiWeather</title>
</head>
<body>
	<%@include file="/WEB-INF/views/common/header.jsp" %>

	첫 시안은 YAMI가 되었습니다. 숭고한 희생...
	<br>
	
	<div id="serverIpDiv">
		<button id="getIpBtn">서버 내부 IP 가져오기</button>
	</div>
  
    <hr>
    perplexity 써먹기
    <form action="${root}/ai/search" method="post">
      입력해요 : <input type="text" name="content" required/> <br />
      <button type="submit">DORO!!</button>
    </form>
    
    
    <hr>
    <a href="${root}/member/login">회원가입</a>
    <a href="${root}/member/enroll">회원가입</a>
    
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
});

</script>
    
  </body>
</html>
