package com.manatoku.mapper;

import java.util.List;
import java.util.Map;

import com.manatoku.model.ChatRoomMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.manatoku.model.Friends;

import com.manatoku.model.ChatMessage;
import com.manatoku.model.ChatRoom;

@Mapper
public interface ChatMapper {
	
	/* SELECT */
	/* 사용자의 그룹채팅 조회 */
	List<ChatRoom> findRoom(@Param("ucode") int ucode);
	/* 사용자가 채팅 참여자인지 조회 */
	int isUserInRoom(@Param("ucode") Integer ucode,@Param("roomId") Integer roomId);
	/* 채팅창의 채팅로그를 조회 */
	List<ChatMessage> selectRecentMessages(@Param("roomId") int roomId,@Param("limit") int limit);
	/* 친구 채팅창 roomId 조회 */
	Integer findDirectRoomId(@Param("ucode") int ucode,@Param("friendUcode") int friendUcode);
	/* 사용자의 그룹 ROLE 조회 */
	String selectUserRole(@Param("roomId") int roomId,@Param("ucode") int ucode);
	/* 사용자의 그룹 인원 조회 */
	int groupMemberCount(@Param("roomId") int roomId);

	/* INSERT */
	/* 사용자 메세지 로그를 저장 */
	void insertMessage
	(@Param("roomId") Integer roomId,@Param("ucode") Integer ucode,@Param("content") String content);
	/* 친구 채팅창 생성 및 roomId 조회 */
	int insertChatRoomDirect(Map<String, Object> param);
	/* 그룹채팅 생성 */
	int createGroup(Map<String, Object> param);
	/* 채팅방에 사용자 추가 */
	void insertRoomMember(@Param("roomId") int roomId,@Param("ucode") int ucode,@Param("role") String role);
	/* 그룹 멤버 조회 */
	List<ChatRoomMember> getGroupMemList(@Param("roomId") int roomId);

	/* UPDATE */
	/* 그룹 이름 바꾸기 */
	void setGroupTitle(@Param("title") String title,@Param("roomId") int roomId);
	/* 그룹장 바꾸기 */
	void changeOwner(@Param("roomId") int roomId);

	/* DELETE */
	/* 그룹에서 나가기 */
	void exitGroup(@Param("roomId") int roomId, @Param("ucode") int ucode);
	/* 그룹 삭제 */
	void deleteGroup(@Param("roomId") int roomId);

	int roomCount(@Param("roomId") int roomId);
	int getSenderUcode();
	Friends selectUserSummary(int friendUcode);

	
}
