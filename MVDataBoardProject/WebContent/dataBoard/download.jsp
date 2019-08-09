<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*, java.io.*, java.net.*"%>
<jsp:useBean id="model" class="com.sist.model.model"/>
<%
try {
	request.setCharacterEncoding("UTF-8");
	String fn = request.getParameter("fn");
	File file = new File("C:\\upload\\"+fn);
	response.setContentLength((int)file.length());
	
	//다운로드창
	response.setHeader("Content-Disposition", "attachment;filename="
			+URLEncoder.encode(fn,"UTF-8"));
	// header 전송 => 다운로드창
	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	
	byte[] buffer = new byte[1024];
	int i = 0;
	while( (i = bis.read(buffer, 0, 1024)) != -1)  //-1 EOF
	{
		bos.write(buffer, 0, i);
	}
	out.clear();
	out = pageContext.pushBody();
	bis.close();
	bos.close();
} catch (Exception e) {
	// TODO: handle exception
	e.printStackTrace();
}
%>