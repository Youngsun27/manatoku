package com.manatoku.mapper;

import java.util.List;

import com.manatoku.model.Friends;
import com.manatoku.model.MemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.manatoku.model.FriendInfo;

@Mapper
public interface FriendsMapper {
	
	/* SELECT */
	/* 친구 목록 불러오기 */
	List<FriendInfo> getFriendsList(@Param("ucode") Integer ucode);

	List<FriendInfo> groupMemAddList(@Param("roomId") int roomId, @Param("ucode") int ucode);

	List<FriendInfo> searchMemberById(@Param("id") String id);

	String checkFriendStatus(@Param("sender") int sender, @Param("receiver") int receiver);

	void insertFriendRequest(@Param("sender") int sender, @Param("receiver") int receiver);

	List<Friends> getReceiveRequest(@Param("receiver") int receiver);

	/* UPDATE */
	/* 친구 신청 수락 */
	void updateFriendStatus(@Param("fcode") int fcode,@Param("ucode") int ucode, @Param("status") String status);

	List<Friends> getFriendList(@Param("ucode") int ucode);

	void deleteFriend(@Param("sender") int sender, @Param("receiver") int receiver);
}
