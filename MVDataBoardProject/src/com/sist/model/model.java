package com.sist.model;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sist.dao.*;

import oracle.net.aso.h;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.sist.dao.*;

import java.net.*;
import java.nio.Buffer;


public class model {
	
	public void databoard_list(HttpServletRequest request){
		String page = request.getParameter("page");
		if(page == null){
			page = "1";
		}
		int curpage = Integer.parseInt(page);
		
		DataBoardDAO dao = new DataBoardDAO();
		List<DataBoardVO> list = dao.databoardListData(curpage);
		int totalpage = dao.databoardTotalPage();
		request.setAttribute("list", list);
		request.setAttribute("curpage", curpage);
		request.setAttribute("totalpage", totalpage);
		
	}
	
	public void dataBoard_insert_ok(HttpServletRequest request, HttpServletResponse response){
		try {
			
			//utf-8로 번경
			request.setCharacterEncoding("UTF-8");
			
			//저장파일지정변수
			String path = "c:\\upload";
			
			//저장 타입지정변수
			String enctype = "UTF-8";
			
			//파일용량 지정 변수
			int size = 100 * 1024 * 1024;
			
			/* 여러파일 받아오는 함수
			 * 업로들할때 필히 써야한다.
			 * DefaultFileRenamePolicy()
			 * 동일 파일이 있을경우 숫자 증가 메소드
			 * ex) name => 다음번에도 name이들어오면 name1
			 */
		MultipartRequest mr = new MultipartRequest(request, path, size, enctype, new DefaultFileRenamePolicy());
		
		
		String name = mr.getParameter("name");
		String pwd = mr.getParameter("pwd");
		String content = mr.getParameter("content");
		String subject = mr.getParameter("subject");
		DataBoardVO vo = new DataBoardVO();
		vo.setName(name);
		vo.setPwd(pwd);
		vo.setContent(content);
		vo.setSubject(subject);
		String filename = mr.getOriginalFileName("upload");
		if (filename == null) {
			vo.setFilename("없음");
			vo.setFilesize(0);
			System.out.println("setFilename = " + vo.getFilename());
			System.out.println("setFilesize = " + vo.getFilesize());
		} else {
			File file =new File(path + "\\" + filename);
			vo.setFilename(filename);
			
			//파일사이즈가 크기는 기본적으로 long이다.
			vo.setFilesize((int)file.length());
			System.out.println("setFilename = " + vo.getFilename());
			System.out.println("setFilesize = " + vo.getFilesize());
		}
		
		DataBoardDAO dao = new DataBoardDAO();
		dao.databoardInsert(vo);
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.err.println("인코딩 에러");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("입출력 에러");
			e.printStackTrace();
		}
	}
	
	public void databoard_detail(HttpServletRequest request){
		
		//값 블러오기
		String no = request.getParameter("no");
		String curpage = request.getParameter("page");
		
		//필요한 클래스 블러오기
		DataBoardDAO dao = new DataBoardDAO();
		DataBoardVO vo = dao.databoardDetailData(Integer.parseInt(no));
		
		request.setAttribute("vo", vo);
		request.setAttribute("curpage", curpage);
		
		//댓글
		List<DataBoardReplyVO> list = dao.databoardReplyData(Integer.parseInt(no));
		request.setAttribute("list", list);
		request.setAttribute("len", list.size() );
	}
	
	//다운로드
	public void download(HttpServletRequest request, HttpServletResponse response) {
			
		
	}
	
	//수정하기전 화면 출력
	public void databoard_updateData(HttpServletRequest request) {
		
		String no  = request.getParameter("no");
		String page  = request.getParameter("page");
		
		//dqo 연동
		DataBoardDAO dao = new DataBoardDAO();
		DataBoardVO vo = dao.databoardUpdate(Integer.parseInt(no));
		
		//request에 있는 데이터를 출력한다. 수정하기전에 값 출력값들
		request.setAttribute("vo", vo);
		request.setAttribute("curpage", page);
	}
	
	public void databoard_update_ok(HttpServletRequest request) {
		
		try{
			request.setCharacterEncoding("UTF-8");
			String name = request.getParameter("name");
			String pwd = request.getParameter("pwd");
			String content = request.getParameter("content");
			String subject = request.getParameter("subject");
			String no = request.getParameter("no"); 
			String page = request.getParameter("page"); 
			DataBoardVO vo = new DataBoardVO();
			vo.setName(name);
			vo.setPwd(pwd);
			vo.setContent(content);
			vo.setSubject(subject);
			vo.setNo(Integer.parseInt(no));
			DataBoardDAO dao=new DataBoardDAO();
		  	boolean bCheck = dao.dataBoardUpdate(vo);
		  	  request.setAttribute("bCheck", bCheck);
		  	  request.setAttribute("curpage", page);
		  	  request.setAttribute("no", no);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 데이터받기
	}
	
	public void login(HttpServletRequest request){
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		//dao 연결
		DataBoardDAO dao = new DataBoardDAO();
		String result = dao.isLogin(id, pwd);
		if (!(result.equals("NOID")||result.equals("NOPWD"))){
			
			// 세션 저장 세션도 리퀘스트도 가져오는것이다
			HttpSession session = request.getSession();
			session.setAttribute("id", id);
			session.setAttribute("name", result);
		}
		request.setAttribute("result", result);
	}
	
	public void reply_insert(HttpServletRequest request){
		
		try{
			request.setCharacterEncoding("UTF-8");
			String msg = request.getParameter("msg");
			String bno = request.getParameter("bno");
			String page = request.getParameter("page");
			
			//리쿼스트는 섹션과 쿠키를 가져올수있다
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			String name = (String)session.getAttribute("name");
			
			DataBoardReplyVO vo = new DataBoardReplyVO();
			vo.setId(id);;
			vo.setMsg(msg);
			vo.setBno(Integer.parseInt(bno));
			vo.setName(name);
			
			// DAO 로 전송 ==> 오라클 연결
			DataBoardDAO dao = new DataBoardDAO();
			dao.replyInsert(vo);
			
			request.setAttribute("bno", bno);
			request.setAttribute("page", page);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void reply_delete(HttpServletRequest request) {
		String no = request.getParameter("no");
		String bno = request.getParameter("bno");
		String page = request.getParameter("page");
		
		//dao => 댓글삭제
		DataBoardDAO dao = new DataBoardDAO();
		dao.replyDelete(Integer.parseInt(no));
		
		//jsp로 보내주기
		request.setAttribute("page", page);
		request.setAttribute("no", bno);
		
	}
	
	 public void reply_update(HttpServletRequest request)
	    {
	    	try
	    	{
	    		request.setCharacterEncoding("UTF-8");
	    	}catch(Exception ex){
	    		ex.printStackTrace();
	    	}
	       	String no=request.getParameter("no");
	    	String bno=request.getParameter("bno");
	    	String page=request.getParameter("page");
	    	String msg=request.getParameter("msg");
	    	// DAO => 댓글 삭제
	    	DataBoardDAO dao=new DataBoardDAO();
	    	System.out.println("리플라이업데이트");
	    	System.out.println("no =" + no);
	    	System.out.println("msg =" + msg);
	    	dao.replyUpdate(Integer.parseInt(no), msg);
	    	// jsp => 필요한 데이터 전송 
	    	request.setAttribute("no", bno);
	    	request.setAttribute("page", page);
	    }
	 
	 public void databoard_delete(HttpServletRequest request){
		 //메가변수를 잡는다 해당 번호와 돌아갈 페이지
		 String no = request.getParameter("no");
		 String page = request.getParameter("page");
		 
		 request.setAttribute("no", no);
		 request.setAttribute("page", page);
	 }
	 
	 public void databoard_delete_ok(HttpServletRequest request) {
		 String no = request.getParameter("no");
		 
		 String page = request.getParameter("page");
		 String pwd = request.getParameter("pwd");
		 
		 System.out.println(no);
		 System.out.println(page);
		 System.out.println(pwd);
		 System.out.println("check");
		 
		 
		 
		 //DB연동해야됨
		 DataBoardDAO dao = new DataBoardDAO();
		 DataBoardVO vo = dao.databoardFileInfo(Integer.parseInt(no));
		 boolean bCheck = dao.databoard_delete(Integer.parseInt(no), pwd);
		 
		 if(bCheck==true) {
			 //파일 사이즈가 존재하는것만 삭제해라
			 if(vo.getFilesize() > 0) {
				 try {
					File file = new File("c:\\upload\\"+vo.getFilename());
					file.delete();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			 }
		 }
		 
		 request.setAttribute("bCheck", bCheck);
		 request.setAttribute("no", no);
		 request.setAttribute("page", page);
	 }
}
