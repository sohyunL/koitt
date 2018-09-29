<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀변호 변경</title>
</head>
<body>
<form action="changePwd" method="post">
<p>
	<input type="password" name="oldPwd" placeholder="현재 비밀번호">
	<c:if test="${errors.oldPwd }">현재 비밀번호를 입력해주세요</c:if>
	<c:if test="${errors.wrongOldPwd }">잘못된 비밀번호 입니다.</c:if>
</p>
<p>
	<input type="password" name="newPwd" placeholder="새 비밀번호">
	<c:if test="${errors.newPwd }">새 비밀번호를 입력해주세요</c:if>
</p>
	<c:if test="${errors.same }">현재 비번과 새 비번이 같습니다. 다시 입력해주세요</c:if>
<input type="submit" value="비밀번호 변경">
</form>
</body>
</html>