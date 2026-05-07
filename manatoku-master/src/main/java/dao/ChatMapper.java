package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import model.Friends;

import model.ChatMessage;
import model.ChatRoom;

public interface ChatMapper {
	
	Integer findDirectRoomId(@Param("ucode") int ucode,@Param("friendUcode") int friendUcode);
	
	List<ChatRoom> findRoom(@Param("ucode") int ucode);
	
	int roomCount(@Param("roomId") int roomId);
	
	int insertChatRoomDirect(Map<String, Object> param);
	
	int createGroup(Map<String, Object> param);
	
	void insertRoomMember(@Param("roomId") int roomId,@Param("ucode") int ucode,@Param("role") String role);
	
	List<ChatMessage> selectRecentMessages(@Param("roomId") int roomId,@Param("limit") int limit);
	
	int getSenderUcode();
	
	Friends selectUserSummary(int friendUcode);
	
	int isUserInRoom(@Param("ucode") Integer ucode,@Param("roomId") Integer roomId);
	
	void insertMessage(@Param("roomId") Integer roomId,@Param("ucode") Integer ucode,@Param("content") String content);
	
	void setGroupTitle(@Param("title") String title,@Param("roomId") int roomId);
	
}
