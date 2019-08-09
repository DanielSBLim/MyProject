<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <jsp:useBean id="model" class ="com.sist.model.model"/>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	model.dataBoard_insert_ok(request, response);
%>



<c:redirect url="list.jsp"/>