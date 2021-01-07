package member.model.dao;

import static common.JDBCTemplate.close;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import member.model.exception.DuplicateMemberIdException;
import member.model.exception.MemberException;

import member.model.vo.Member;

/**
 * DAO
 * 3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
 * 4. 쿼리전송(실행) - 결과값
 * 4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
 * 5. 자원반납(PreparedStetement, ResultSet)
 */
public class MemberDAO {
	
	private Properties prop = new Properties();
	
	public MemberDAO() {
		try {
			prop.load(new FileReader("resources/query.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int insertMember(Connection conn, Member member) throws MemberException {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
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
		
		} catch(SQLIntegrityConstraintViolationException e) {
			if(e.getMessage().contains("STUDENT.PK_MEMBER_ID"))
				throw new DuplicateMemberIdException("중복된 아이디 : " + member.getMemberId(), e);
				
		} catch (Exception e) {
			throw new MemberException("회원가입 오류! 관리자에게 문의하세요.", e);
		} finally {
			//5. 자원반납(PreparedStetement, ResultSet)
			close(pstmt);
		}
		return result;
	}

	public Member selectOneMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectOneMember");
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
				member.setDel_date(rset.getDate("del_date"));
	            member.setDel_flag(rset.getString("del_flag"));
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

	public List<Member> selectALL(Connection conn) throws MemberException {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectALL");
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
            member.setDel_date(rset.getDate("del_date"));
            member.setDel_flag(rset.getString("del_flag"));
            
            list.add(member);
			}
		} catch (Exception e) {
			//e.printStackTrace();
//			throw e; //1. 해당 에외를 다시 던지기
			throw new MemberException("회원조회 오류! 관리자에게 문의하세요.", e);//2.구체적인 커스텀 예외클래스를 생성해서 던지기,e원래발생했던예외
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
		String sql = prop.getProperty("deleteMember");
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
		String sql = prop.getProperty("selectMemberByName");
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
				member.setDel_date(rset.getDate("del_date"));
	            member.setDel_flag(rset.getString("del_flag"));
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
		String sql = prop.getProperty("newPassword");
		
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			System.out.println("query@dao = " + sql);
			
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
		String sql = prop.getProperty("newEmail");
		
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
		String sql = prop.getProperty("newPhone");
		
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
		String sql = prop.getProperty("newAddress");
		
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

	public List<Member> selectDeletedMember(Connection conn) throws MemberException {
		List<Member> list = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectDeletedMember");
		try {
			//3. 쿼리문 생성 및 Statement객체 (PreaparedStatement)생성 
			pstmt= conn.prepareStatement(sql);
			rset=pstmt.executeQuery();
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
				member.setDel_date(rset.getDate("del_date"));
				member.setDel_flag(rset.getString("del_flag"));
				System.out.println(member);
				list.add(member);
			}			
			
		} catch (Exception e) {
			throw new MemberException("회원 삭제 오류! 관리자에게 문의하세요.", e);
		} finally {
			close(rset);
			close(pstmt);
		}		
		
		return list;
	}

	

}