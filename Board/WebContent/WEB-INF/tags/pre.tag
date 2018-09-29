<%@ tag body-content="empty" pageEncoding="utf-8"%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" type="java.lang.String" required="true" %>
<%
	// 치환
	value=value.replace("&" , "&amp;");
	value=value.replace("<" , "&lt;");
	value=value.replace(">" , "&gt;");
	value=value.replace("\n" , "<br>");
	value=value.replace(" " , "&nbsp;");
%>
<%=value%>