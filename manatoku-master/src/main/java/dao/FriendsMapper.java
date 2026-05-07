package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.Member;

public interface FriendsMapper {
	
	List<Member> getFriendsList(@Param("ucode") Integer ucode);
	
}
