<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>header</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/lodash@4.17.21/lodash.min.js"></script>
</head>
<body>
<c:set var="root" value="${pageContext.request.contextPath}"/>
<c:set var="loginUser" value="${sessionScope.loginUser}"/>
<script>
	let msg = "${alertMsg}"
	if(msg!=""){
		alert(msg);
	}
</script>
<c:remove var="alertMsg"/>
</body>
</html>