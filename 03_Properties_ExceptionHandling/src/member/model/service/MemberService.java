package member.model.service;

//클래스명 없이 매소드를 사용할수있다
import static common.JDBCTemplate.close;
import static common.JDBCTemplate.commit;
import static common.JDBCTemplate.getConnection;
import static common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import member.model.dao.MemberDAO;
import member.model.exception.MemberException;
import member.model.vo.Member;

/**
 * 
 * Service
 * 업무로직 담당클래스 (connectiont생성, 트랜잭션, DAO업무요청)
 * 
 * 1. jdbc driver 클래스 등록(dbms별로 제공) : 최초 1회
 * 2. db connection객체 생성 : dbserver url, user, password
 * -> DAO담당
 * 6. 트랜잭션처리(commit, rollback)
 * 7. 자원반납(Connection) 
 *
 */
public class MemberService {
	
	private MemberDAO memberDAO = new MemberDAO();
	//JDBC 만들어서 얼마나 간단해졌는지
	//import static common.JDBCTemplate.*; 이걸 함으로써
	//JDBCTemplate.close(000) ->close(000) 으로 사용가능
	public int insertMember(Member member) throws MemberException {
		//1.Connection 생성
		//Connection conn = JDBCTemplate.getConnection();
		Connection conn = getConnection();
		//2. dao요청
		int result = memberDAO.insertMember(conn, member);
		//3. 트랜잭션 처리
		//if(result  > 0) JDBCTemplate.commit(conn);
		//else JDBCTemplate.rollback(conn);
		if(result  > 0) commit(conn);
		else rollback(conn);
		//4. 자원반납
		//conn.close();이거였음
		//JDBCTemplate.close(conn);
		close(conn);
		return result;
	}

	public int insertMember_(Member member) {
		String driverClass = "oracle.jdbc.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "student";
		String password = "student";
		Connection conn = null;
		int result = 0;
		
		try {
			//1. jdbc driver 클래스 등록(dbms별로 제공) : 최초 1회
			Class.forName(driverClass);
			//2. db connection객체 생성 : dbserver url, user, password
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false); // 각각을 따로 commit/rollback 할 수 있게 설정
			//자동금지
			//dao요청
			result = memberDAO.insertMember(conn, member);
			//6. 트랜잭션처리(commit, rollback)
			if(result  > 0) conn.commit();
			else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//7. 자원반납(Connection) 
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public Member selectOneMember(String memberId) {
		//1.Connection생성
		Connection conn = getConnection();
		//2.dao요청
		Member member = memberDAO.selectOneMember(conn, memberId);
		//3.자원반납 - select문이랑 트랜잭션 필요없다
		close(conn);
		
		return member;
	}

	public List<Member> selectALL() throws MemberException {
		//1.Connection생성
		Connection conn = getConnection();
		//2.dao요청
		List<Member> list = memberDAO.selectALL(conn);
		//3.자원반납 - select문이랑 트랜잭션 필요없다
		close(conn);
		
		return list;
	}


	public int deleteMember(String memberId) {
		Connection conn = getConnection();
		int result = memberDAO.deleteMember(conn, memberId);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public List<Member> selectMemberByName(String name) {
		Connection conn = getConnection();
		List<Member> list = memberDAO.selectMemberByName(conn, name);
		close(conn);
		return list;
	}

	public int newPassword(String password, Member member) {
		Connection conn = getConnection();
		int result = memberDAO.newPassword(conn,password, member);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public int newEmail(String email, Member member) {
		Connection conn = getConnection();
		int result = memberDAO.newEmail(conn,email, member);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public int newPhone(String phone, Member member) {
		Connection conn = getConnection();
		int result = memberDAO.newPhone(conn,phone, member);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public int newAddress(String address, Member member) {
		Connection conn = getConnection();
		int result = memberDAO.newAddress(conn,address, member);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public List<Member> selectDeletedMember() throws MemberException{
		Connection conn = getConnection();
		List<Member> list = memberDAO.selectDeletedMember(conn);
		close(conn);
		return list;
	}


}