package member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static common.JDBCTemplate.*;
import member.model.vo.Member;

/**
 * DAO
 * 3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
 * 4. 쿼리전송(실행) - 결과값
 * 4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
 * 5. 자원반납(PreparedStetement, ResultSet)
 */
public class MemberDAO {

	public int insertMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "insert into member values(?,?,?,?,?,?,?,?,?,default)";
		try {
			//3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
			//4. 쿼리전송(실행) - 결과값
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				
		//5. 자원반납(PreparedStetement, ResultSet)
			//pstmt.close();
			//import static common.JDBCTemplate.*;이거했기때문에
				close(pstmt);
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
		return result;
	}

	public Member selectOneMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where member_id = ?";
		Member member = null;
		//1.PreparedStatement 생성, 미완성 쿼리 값대입
		try {
			//1.PreparedStatement 생성, 미완성 쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			//2. 실행 및 ResultSet값 -> member 객체
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				member = new Member();
				member.setMemberId(memberId);
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				//3. 자원반납
				close(rset);
				close(pstmt);
			}catch (Exception e) {
	            e.printStackTrace();
	         }
		}
		//System.out.println("member@dao = " + member);		
		return member;
	}

	public List<Member> selectALL(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member order by enroll_date desc";
		List<Member> list = null;
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			list = new ArrayList<>();
			while(rset.next()) {
			Member member = new Member(); 
			
			member.setMemberId(rset.getString("member_id"));
            member.setPassword(rset.getString("password"));
            member.setMemberName(rset.getString("member_name"));
            member.setGender(rset.getString("gender"));
            member.setAge(rset.getInt("age"));
            member.setEmail(rset.getString("email"));
            member.setPhone(rset.getString("phone"));
            member.setAddress(rset.getString("address"));
            member.setHobby(rset.getString("hobby"));
            member.setEnrollDate(rset.getDate("enroll_date"));
            
            list.add(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				close(rset);
				close(pstmt);
				close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}


	public int deleteMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "delete from member where member_id = ?";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			result = pstmt.executeUpdate();
		
			if(result > 0) conn.commit();
	         else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				close(rset);
				close(pstmt);
				close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public List<Member> selectMemberByName(Connection conn, String name) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where member_name = ?";
		List<Member> list = new ArrayList<Member>();
		Member member = null;
		
		try {
			//1. PrepareStatement 생성, 미완성 쿼리 값 대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			//2. 실행 및 ResultSet값 -> member객체
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				member = new Member();
				member.setMemberId(rset.getString("member_id"));
				member.setPassword(rset.getString("password"));
				member.setMemberName(name);
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				list.add(member);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//3. 자원반납
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int newPassword(Connection conn, String password, Member member) {
		PreparedStatement pstmt =null;
		int result =0;
		String sql = "update member set password = ? where member_id= ? ";
		
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, password);
			pstmt.setString(2, member.getMemberId());
			result=pstmt.executeUpdate();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int newEmail(Connection conn, String email, Member member) {
		PreparedStatement pstmt =null;
		int result =0;
		String sql = "update member set email = ? where member_id= ? ";
		
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, member.getMemberId());
			result=pstmt.executeUpdate();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int newPhone(Connection conn, String phone, Member member) {
		PreparedStatement pstmt =null;
		int result =0;
		String sql = "update member set phone = ? where member_id= ? ";
		
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, phone);
			pstmt.setString(2, member.getMemberId());
			result=pstmt.executeUpdate();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int newAddress(Connection conn, String address, Member member) {
		PreparedStatement pstmt =null;
		int result =0;
		String sql = "update member set address = ? where member_id= ? ";
		
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, address);
			pstmt.setString(2, member.getMemberId());
			result=pstmt.executeUpdate();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	

}