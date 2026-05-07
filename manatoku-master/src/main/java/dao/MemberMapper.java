package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.Member;

public interface MemberMapper {
	
	int insertMember(Member member);
	
	int checkId(String id);
	
	int checkEmail(String email);
	
	String getPass(String id);
	
	String getPassWithUcode(int ucode);
	
	Member selectMemberById(String id);
	
	void deleteMember(int ucode);
	
	void updateName(@Param("ucode") int ucode,@Param("name") String name);

}
