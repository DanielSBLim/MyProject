<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:useBean id="model" class="com.sist.model.model" scope="request"/>
    <%
     	model.databoard_updateData(request);
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel = "styleSheet" href ="css/bootstrap.min.css">
<style type="text/css">
	.row {
	margin: 0px auto;
	width : 700px;
	}
</style>
</head>
<body>
	<div class = "container">
		<h2 class = "text-center">수정하기</h2>
		<div class = row>
			<form method ="post" action ="update_ok.jsp" >
			<input type="hidden" name = "no" value ="${vo.no }">
			<input type="hidden" name = "page" value ="${curpage }">
				<table class = table>
					<tr>
						<th class ="text-right info" width="15%">이름</th>
						<td width="85%">
							<input type="text" name = "name" size = 15 value="${vo.name }">
						</td>
					</tr>
					<tr>
						<th class ="text-right info" width="15%">제목</th>
						<td width="85%">
							<input type="text" name = "subject" size = 45 value="${vo.subject }">
						</td>
					</tr>
					<tr>
						<th class ="text-right info" width="15%">내용</th>
						<td width="85%">
							<textarea name = "content" rows = "10" cols ="55">${vo.content }</textarea>
						</td>
					</tr>
					<tr>
						<th class ="text-right info" width="15%">비밀번호</th>
						<td width="85%">
							<input type="password" name = "pwd" size = 10>
						</td>
					</tr>
					
					
					<tr>
						<td colspan="2" class="text-center">
							<input type="submit" value="수정하기" class="btn btn-sm btn-primary">
							<a href="datail.jsp?no=${vo.no }&page=${curpage }">
								<input type="button" value="취소" class="btn btn-sm btn-danger">
							</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>