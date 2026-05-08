package com.manatoku.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.manatoku.model.Member;

@Mapper
public interface MemberMapper {
	
	/* SELECT */
	/* 중복 체크 */
	int checkId(@Param("id") String id); //아이디 중복체크
	int checkEmail(@Param("email") String email); //이메일 중복체크
	/* 사용자 조회 */
	Member selectMemberById(@Param("id") String id);
	Member selectMemberByUcode(@Param("ucode") int ucode);
	/* 비밀번호 조회 */
	String getPassWithUcode(@Param("ucode") int ucode);
	
	/* UPDATE */
	/* 사용자 수정 */
	void updateName(@Param("ucode") int ucode,@Param("name") String name);
	/* 사용자 수정 (이름,생일,전화번호) */
	void updateMemberInfo(@Param("ucode") int ucode,@Param("pass") String pass,@Param("name") String name, @Param("birth") String birth,@Param("phone") String phone);
	/* 사용자 비밀번호 수정 */
	void updatePassword(@Param("ucode") int ucode,@Param("pass") String pass);
	
	
	/* DELETE */
	/* 사용자 추가 */
	int insertMember(Member member);
	
	/* INSERT */
	/* 사용자 삭제 */
	void deleteMember(@Param("ucode") int ucode);
	
	

}
