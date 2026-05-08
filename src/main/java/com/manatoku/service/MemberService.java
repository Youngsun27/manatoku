package com.manatoku.service;

import com.manatoku.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manatoku.mapper.MemberMapper;
import com.manatoku.model.Member;
import com.manatoku.model.MemberForm;
import com.manatoku.model.MemberResponse;
import com.manatoku.serviceModel.ServiceResult;
import org.springframework.web.bind.annotation.ResponseBody;

/* SringMVC에 @Service 종속주입 */
@Service
public class MemberService {

	/* SpringMVC에 종속 주입 MemberMapper의 기능은 model의 MemberMapper 클래스 참조 */
	private final MemberMapper mapper;

	public MemberService(MemberMapper mapper) {
		this.mapper = mapper;
	}

	/*
	 * 멤버 로그인 동작 LoginResult 객체는 DB 동작 결과를 기록하는 객체로 자세한 내용은 model의 LoginResult 클래스
	 * 참조
	 */
	@Transactional(readOnly = true)
	public ServiceResult<Member> login(String id, String pw) {
		/* 아이디 체크 */
		Member member = mapper.selectMemberById(id); // 사용자 정보 조회
		if (member == null) { // 존재하지 않은 아이디
			throw new MemberNotFoundException("IDが正しくありません。");
		}
		if (pw.equals(member.getPass())) { // 비밀번호 일치
			return ServiceResult.success(member);
		} else { // 비밀번호 불일치
			throw new MemberNotFoundException("パスワードが正しくありません。");
		}

	}

	/*
	 * 멤버 회원가입 동작
	 * 
	 * @Transactional는 메소드내의 동작이 정상적으로 수행시 자동 커밋 실패시 롤백 RegisterResult 객체는 DB 동작 결과를
	 * 기록하는 객체로 자세한 내용은 model의 RegisterResult 클래스 참조
	 */
	@Transactional
	public ServiceResult<Void> register(Member member) {
		/* 아이디 중복 체크 */
		int res = mapper.checkId(member.getId());
		if (res > 0) { // 이미 존재하는 아이디
			/* 에러 메세지와 함께 리턴 */
			throw new MemberNotFoundException("このIDは登録できません。");
		}

		/* 이메일 중복 체크 */
		res = mapper.checkEmail(member.getEmail());
		if (res > 0) { // 이미 존재하는 이메일
			/* 에러 메세지와 함께 리턴 */
			throw new MemberNotFoundException("このメールアドレスは登録できません。");
		}

		/* DB에 사용자 정보 입력 */
		mapper.insertMember(member);

		/* member 객체와 함께 리턴 */
		return ServiceResult.success();

	}

	@Transactional
	public ServiceResult<Member> updateMember(int ucode, MemberForm form) {
		// 현재 비밀번호 확인
		
		Member member = mapper.selectMemberByUcode(ucode);

		if (!form.getPasswd().equals(member.getPass())) { // 비밀번호가 일치하지 않을 경우
			throw new MemberNotFoundException("パスワードが正しくありません。"); // false 리턴
		}
		
		member.setName(form.getName());
		member.setBirth(form.getBirth());
		member.setPhone(form.getPhone());
		
		String changePw;
		
		if (form.getNewPwd() != null && !form.getNewPwd().trim().isEmpty()) {
			changePw = form.getNewPwd();
		}
		else {
			changePw = member.getPass();
		}

		mapper.updateMemberInfo(ucode, changePw,
				member.getName(), member.getBirth(), member.getPhone());

		return ServiceResult.success(member);

	}

	// 멤버 탈퇴 동작
	@Transactional
	public ServiceResult<Void> withdraw(int ucode, String pw) {
		// DB 비밀번호 조회
		String dbPw = mapper.getPassWithUcode(ucode);

		// 비밀번호 불일치
		if (!pw.equals(dbPw)) {
			throw new MemberNotFoundException("パスワードが正しくありません。");
		}
		// 회원 삭제
		mapper.deleteMember(ucode);

		return ServiceResult.success();

	}

	/* Model 변환 메소드 */
	/* Form => Model */
	public Member formToModel(MemberForm form) {
		Member member = new Member();

		member.setEmail(form.getEmail());
		member.setId(form.getId());
		member.setName(form.getName());
		member.setPass(form.getPasswd());
		member.setPhone(form.getPhone());
		member.setBirth(form.getBirth());

		return member;
	}

	/* Model => Response */
	public MemberResponse modelToResponse(Member member) {
		MemberResponse response = new MemberResponse();
		response.setUcode(member.getUcode());
		response.setEmail(member.getEmail());
		response.setId(member.getId());
		response.setName(member.getName());
		String birth = member.getBirth();
		String[] b = (birth == null) ? new String[0] : birth.split("-");
		response.setBirth1((b.length > 0 ? b[0] : ""));
		response.setBirth2((b.length > 1 ? b[1] : ""));
		response.setBirth3((b.length > 2 ? b[2] : ""));
		String phone = member.getPhone();
		String[] p = (phone == null) ? new String[0] : phone.split("-");
		response.setPhone1((p.length > 0 ? p[0] : ""));
		response.setPhone2((p.length > 1 ? p[1] : ""));
		response.setPhone3((p.length > 2 ? p[2] : ""));

		return response;
	}

}
