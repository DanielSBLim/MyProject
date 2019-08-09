package com.sist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sun.crypto.provider.RSACipher;

import java.util.*;


public class DataBoardDAO {
	//����
	private Connection conn;
	private PreparedStatement ps; //inputStream(���� �о�´�), outputStream(sql���� ����)
	private final String URL = "jdbc:oracle:thin:@localhost:1522:ORCL";
		//new Socket("ip",��Ʈ��ȣ) : ��Ʈ��ȣ (0~65335) : 0 ~ 1024 => 1521, 1433, 7000, 8080
		//27017 (������)
	
	//����̹� ���
	/*
	 * thin, oci
	 * thin : ���Ḹ���ش�.
	 * oci : �����͸� �����´�
	 */
	public DataBoardDAO() {
		// TODO Auto-generated constructor stub
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");//���÷���
			// Ŭ������ �̸��� �о Ŭ������ ���� => Spring
			// �޸� �Ҵ� => new�� ������� �ʰ� �޸� �Ҵ�
			// => �ݵ�� ��Ű������� ~ Ŭ���� ��
			// <jsp:useBean id ="" class="" >
			// 1. ���ռ� (������) ���� => ������ ���� ���α׷�
			// 2. �������� ���ϰ� ==> �޼ҵ� (�Ѱ��� ��ɸ� ������ �����ϰ� �����)
		} catch (Exception ex) {
			
			ex.printStackTrace();
		}
	}
	//����
	public void getConnection() {
		
		try {
			
			conn = DriverManager.getConnection(URL, "scott", "tiger");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	//����
	public void disConnection() {
		
		try {
			
			if (ps != null) ps.close();
			if (conn != null) conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	//���
	public List<DataBoardVO> databoardListData(int page) {
		List<DataBoardVO> list = new ArrayList<DataBoardVO>();
		try {
			getConnection();
			String sql = "SELECT no, subject, name, regdate, hit, num "
					+ "FROM (Select no, subject, name, regdate, hit, rownum as num "
					+ "FROM (Select no, subject, name, regdate, hit "
					+ "FROM databoard ORDER BY no DESC)) "
					+ "WHERE num BETWEEN ? AND ? ";
			/*
			 *  view => �������̺�
			 *   = ���Ϻ� : ���̺� �Ѱ��� ����� ���
			 *   Create view emp_view
				 as select * from emp;
			 *   = ���պ� : ���̺� ������ ���� (join, subQuery)
			 *   = �ζ��κ� (****) FROM (SELECT ~)
			 */
			
			ps = conn.prepareStatement(sql);
			
			//?�� ��ä���� �����ϱ�
			int rowSize = 10;
			int end = (rowSize * page);
			int start = end - (rowSize - 1);

			ps.setInt(1, start);
			ps.setInt(2, end);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				DataBoardVO vo = new DataBoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	public void databoardInsert(DataBoardVO vo) {
		try {
			getConnection();
			String sql = "INSERT INTO DATABOARD(no, name, subject, content, pwd, filename, filesize) "
					+ "VALUES((SELECT NVL(MAX(no)+1,1) FROM dataBoard) "
					+ ", ?, ?, ?, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			ps.setString(5, vo.getFilename());
			ps.setInt(6, vo.getFilesize());
			ps.executeUpdate();

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public DataBoardVO databoardDetailData(int no){
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();
			String sql = "UPDATE DATABOARD set "
					+ "hit = hit + 1 "
					+ "where no = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate(); //��ȸ�� ����
			
			sql = "select no, name, subject, content, regdate, filename, filesize, hit "
					+ "From databoard "
					+ "where no = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setFilename(rs.getString(6));
			vo.setFilesize(rs.getInt(7));
			vo.setHit(rs.getInt(8));
			
			rs.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		
		return vo;
	}
	
	public int databoardTotalPage(){
		int total = 0;
		try {
			getConnection();
			String sql = "select ceil(count(*)/10.0) from databoard ";
			
			//ceil, round, trunc
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			total = rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		
		return total;
	}
	
	//������Ʈ ������
	public DataBoardVO databoardUpdate(int no) {
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();
			
			String  sql = "select no, name, subject, content "
					+ "From databoard "
					+ "where no = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			rs.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		
		return vo;
	}
	
	
	//dao => model = > jsp
	public boolean dataBoardUpdate(DataBoardVO vo){
		boolean bCheck = false;
		try{
			getConnection();
			
			//��й�ȣ �˻�
			String sql = "SELECT pwd FROM databoard "
					+ "where no = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, vo.getNo());
	
			ResultSet rs = ps.executeQuery();
			rs.next();
			String db_pwd = rs.getString(1);
			rs.close();
			
			if(db_pwd.equals(vo.getPwd())){
				bCheck = true;
				
				sql = "update databoard set name =? , subject =?, content =? "
						+ "where no = ?" ;
				ps = conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getSubject());
				ps.setString(3, vo.getContent());
				ps.setInt(4, vo.getNo());
				ps.executeUpdate();
				
			} else {
				bCheck = false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return bCheck;
	}
	
	//login 
	//��쿡 ���� 2�� �̻��̸� String �̳� int�� ó���Ѵ�
	public String isLogin(String  id, String pwd) {
		String result = "";
		try{
			getConnection();
			String sql = "select count(*) "
					+ "from member where id = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
			
			if(count == 0) {
			
				result = "NOID";
			} else {
				sql = "select pwd, name "
						+ "from member where id = ? ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				rs.next();
				String db_pwd = rs.getString(1);
				String name = rs.getString(2);
				rs.close();
				
				if(db_pwd.equals(pwd)){
					result = name;
				} else {
					result = "NOPWD";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		
		return result;
	}
	
	//��� ����
	public List<DataBoardReplyVO> databoardReplyData(int bno){
		List<DataBoardReplyVO> list = new ArrayList<DataBoardReplyVO>();
		try{
			getConnection();
			String sql = "select no, bno, id, name, msg, TO_CHAR(regdate, 'YYYY-MM-DD HH24:MI:SS') "
					+ "from DATABOARDREPLY where bno = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, bno);
			ResultSet rs =  ps.executeQuery();
			while(rs.next()){
				DataBoardReplyVO vo = new DataBoardReplyVO();
				vo.setNo(rs.getInt(1));
				vo.setBno(rs.getInt(2));
				vo.setId(rs.getString(3));
				vo.setName(rs.getString(4));
				vo.setMsg(rs.getString(5));
				vo.setDbday(rs.getString(6));
				list.add(vo);
			}
			
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			disConnection();
		}
		return list;
	}
	
	//��� �߰�
	public void replyInsert(DataBoardReplyVO vo){
		
		try {
			getConnection();
			String sql = "insert into DATABOARDREPLY values( "
					+ "(select nvl(max(no)+1, 1) from DATABOARDREPLY), "
					+ "?, ?, ?, ?, sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, vo.getBno());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getMsg());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			disConnection();
		}
		
	}
	
	//��� ����
	public void replyDelete(int no){
		
		try {
			getConnection();
			String sql = "delete from databoardReply Where no = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			disConnection();
		}
		
	}
	
	public void replyUpdate(int no, String msg) {
		try {
			getConnection();
			String sql="UPDATE databoardReply SET msg=? WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, msg);
			ps.setInt(2, no);
			ps.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
	// 1. ���ϻ��� 2.��ۻ��� 3. ���� �Խù� ���� => ��й�ȣ�� �´°��
	public DataBoardVO databoardFileInfo(int no) {
		DataBoardVO vo = new DataBoardVO();
		try{
			getConnection();
			String sql = "SELECT filename, filesize FROM databoard "
					+ "WHERE no = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs = ps.executeQuery();
			rs.next();
			vo.setFilename(rs.getString(1));
			vo.setFilesize(rs.getInt(2));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			disConnection();
		}
		
		
		return vo;
	}
	
	public boolean databoard_delete(int no, String pwd){
		boolean bCheck = false;
		 try{
			 getConnection();
			 //��й�ȣ �˻����
			 String sql = "SELECT pwd "
			 		+ "FROM databoard "
			 		+ "WHERE no = ?";
			 ps = conn.prepareStatement(sql);
			 ps.setInt(1, no);
			 ResultSet rs = ps.executeQuery();
			 rs.next();
			 String db_pwd = rs.getString(1);
			 rs.close();
			 
			 if(db_pwd.equals(pwd)){
				 //����
				 bCheck = true;
				 sql = "SELECT count(*) "
				 		+ "FROM databoardReply "
				 		+ "WHERE bno = ? ";
				 ps = conn.prepareStatement(sql);
				 ps.setInt(1, no);
				 rs = ps.executeQuery();
				 rs.next();
				 int count = rs.getInt(1);
				 rs.close();
				 
				 if(count != 0) {
					 sql ="DELETE FROM databoardReply "
					 		+ "WHERE bno = ? ";
					 ps = conn.prepareStatement(sql);
					 ps.setInt(1, no);
					 ps.executeUpdate();
				 }
				 
				 //���� �Խù�
				 sql = "DELETE FROM databoard "
				 		+ "WHERE no = ?";
				 ps = conn.prepareStatement(sql);
				 ps.setInt(1, no);
				 ps.executeUpdate();
			 }
			 
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		} finally {
			disConnection();
		}
		return bCheck;
	}
}
