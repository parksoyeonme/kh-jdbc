package member.controller;

import java.util.List;

import member.model.exception.MemberException;
import member.model.service.MemberService;
import member.model.vo.Member;
import member.view.MemberMenu;

public class MemberController {

	private MemberService memberService = new MemberService();
	
	public int insertMember(Member member) {
		int result = 0;
		try {
			result = memberService.insertMember(member);
		} catch (MemberException e) {
			e.printStackTrace();
			new MemberMenu().displayError(e.getMessage());
		}
		return result;
	}

	public Member selectOneMember(String MemberId) {
		return memberService.selectOneMember(MemberId);
	}

	public List<Member> selectALL() {
		List<Member> list = null;
		try {
			list = memberService.selectALL();
		} catch (MemberException e) {
			new MemberMenu().displayError(e.getMessage());//사용자에게 오류메세지를 알려주는것
		}
		return list;
	}


	public int deleteMember(String memberId) {
		return memberService.deleteMember(memberId);
	}

	public List<Member> selectMemberByName(String name) {
		return memberService.selectMemberByName(name);
	}

	public int newPassword(String password, Member member) {
		return memberService.newPassword(password,member);
	}

	public int newEmail(String email, Member member) {
		return memberService.newEmail(email,member);
	}

	public int newPhone(String phone, Member member) {
		return memberService.newPhone(phone,member);
	}

	public int newAddress(String address, Member member) {
		return memberService.newAddress(address,member);
	}

	public List<Member> selectDeletedMember() {
		List<Member> list = null;
		try {
			list = memberService.selectDeletedMember();
		} catch (MemberException e) {
			new MemberMenu().displayError(e.getMessage());//사용자에게 오류메세지를 알려주는것
		}
		return list;
	}
}