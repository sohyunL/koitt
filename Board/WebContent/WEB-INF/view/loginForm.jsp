<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<form action="login" method="post">
<c:if test="${errors.idOrPwNotMatch }">아이디와 비밀번호가 일치하지 않습니다.</c:if>
<p>
	<input type="text" name="loginId" value="${param.loginId }" placeholder="아이디">
	<c:if test="${errors.loginId }">아이디를 입력하세요</c:if>
</p>
<p>
	<input type="password" name="password" value="${param.password }" placeholder="비밀번호">
	<c:if test="${errors.password }">비밀번호를 입력하세요</c:if>
</p>
<input type="submit" value="로그인">
</form>
</body>
</html>