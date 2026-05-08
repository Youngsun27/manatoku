package com.manatoku.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manatoku.mapper.ChatMapper;
import com.manatoku.mapper.FriendsMapper;
import com.manatoku.model.ChatRoom;
import com.manatoku.model.FriendInfo;

@Service
public class ApiService {
	
	/* SpringMVC에 의존성 주입 FriendsMapper의 기능은 model의 FriendsMapper 클래스 참조 */
	private final FriendsMapper friendMapper;
	private final ChatMapper chatMapper;
    public ApiService(FriendsMapper friendMapper, ChatMapper chatMapper) {
        this.friendMapper = friendMapper;
        this.chatMapper = chatMapper;
    }
    
    /* ucode를 키값으로 사용자의 친구 목록을 DB에서 검색 */
    @Transactional(readOnly = true)
    public List<FriendInfo> getFriendList(int ucode) {
    		List<FriendInfo> list = friendMapper.getFriendsList(ucode);
    		
    		return list;
    }
    
    /* ucode를 키값으로 사용자의 그룹채팅 목록을 DB에서 검색 */
    @Transactional(readOnly = true)
    public List<ChatRoom> getGroupList(int ucode) {
    	/* ucode를 키값으로 사용자의 그룹채팅 목록을 DB에서 검색 */
    	List<ChatRoom> list = chatMapper.findRoom(ucode);
    	
    	return list;
    }

    @Transactional(readOnly = true)
    public List<FriendInfo> groupMemAddList(int roomId,int ucode){
        List<FriendInfo> list = friendMapper.groupMemAddList(roomId,ucode);

        return list;
    }
	

}
