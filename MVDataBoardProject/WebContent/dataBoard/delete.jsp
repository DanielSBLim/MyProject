<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <jsp:useBean id="model" class="com.sist.model.model"/>
    <%
    	model.databoard_delete(request);
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<style type="text/css">
.row {
   margin: 0px auto;
   width:300px;
}
</style>
</head>
<body>
	<%-- 삭제하기 --%>
  <div class="container">
    <h2 class="text-center">삭제하기</h2>
    <div class="row">
    <form method="post" action="delete.ok.jsp">
       <table class="table">
        <tr>
          <td class="text-right" width=30%>비밀번호</td>
          <td class="text-left" width=70%>
            <input type=password name=pwd size=15>
            <input type="hidden" name=no size=15 value="${no}">
            <input type="hidden" name=page size=15 value="${page}">
          </td>
        </tr>
        <tr>
          <td class="text-center" colspan="2">
            <input type=submit value="삭제" class="btn btn-sm btn-danger">
            <input type=button value="취소" class="btn btn-sm btn-primary"
              onclick="javascript:history.back()"
            >
          </td>
        </tr>
      </table>
      </FORM>
    </div>
  </div>
</body>
</html>









