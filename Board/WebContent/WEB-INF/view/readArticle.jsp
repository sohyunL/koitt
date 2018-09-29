<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %> <!-- u = 사용자 태그를 사용할 접두어,  /WEB-INF/tags = 태크파일이 있는 폴더를 지정 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<table border="1">
<tr>
	<th>번호</th>
	<td>${articleData.article.articleId }</td>
</tr>	
<tr>
	<th>작성자</th>
	<td>${articleData.article.writer.name }</td>
</tr>	
<tr>
	<th>제목</th>
	<td>${articleData.article.title }</td>
</tr>	
<tr>
	<th>내용</th>
	<td><u:pre value="${articleData.content }"></u:pre></td>
</tr>	
<tr>
	<td colspan="2">
	<c:set var="pageNo" value="${empty param.pageNo ? '1' : param.pageNo }"/>
	<a href="list?pageNo=${pageNo }">[목록]</a>
	<c:if test="${authUser.userId == articleData.article.writer.writerId }">
		<a href="modify?no=${articleData.article.articleId }">[게시글 수정]</a>
		<a href="delete?no=${articleData.article.articleId }">[게시글 삭제]</a>
	</c:if>
	</td>
</tr>	

</table>

</body>
</html>