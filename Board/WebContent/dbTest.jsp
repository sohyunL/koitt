<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="jdbc.ConnectionProvider"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>DB Connect 확인</h1>

	<%
	
		try(Connection conn = ConnectionProvider.getConnection()){
			out.println("커넥션 받아오기 성공");
		}catch(SQLException e){
			out.println("커넥션 받아오기 실패" + e.getMessage());
		}
	
	%>

</body>
</html>