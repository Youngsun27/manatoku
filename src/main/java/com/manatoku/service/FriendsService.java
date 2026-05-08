package com.manatoku.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manatoku.mapper.FriendsMapper;
import com.manatoku.model.Friends;
import com.manatoku.model.FriendInfo;

@Service
public class FriendsService {

    private final FriendsMapper mapper;
    public FriendsService(FriendsMapper mapper) {
        this.mapper = mapper;
    }

    /* 1~2. 친구 ID 검색 */
    public List<FriendInfo> searchMemberById(String id) {
        return mapper.searchMemberById(id);
    }


    /* 3. 친구 신청 */
    @Transactional
    public boolean sendFriendRequest(int sender, int receiver) {

        /* 친구 상태 확인해서 status에 저장 */
        String status = mapper.checkFriendStatus(sender, receiver);

        /* 이미 친구거나 거절이면 false 리턴 */
        if (status != null && !"REJECT".equals(status)) {
            return false;
        }

        /* 친구가 아닌 경우에 친구신청 */
        mapper.insertFriendRequest(sender, receiver);
        return true;
    }


    /* 4. 받은 친구 신청 */
    public List<Friends> getReceiveRequest(int myUcode) {
        return mapper.getReceiveRequest(myUcode);
    }


    /* 5. 수락 / 거절 */
    @Transactional
    public void updateFriendStatus(int fcode, int ucode, String status) {
        mapper.updateFriendStatus(fcode,ucode, status);
    }


    /* 6. 친구 삭제 */
    @Transactional
    public void deleteFriend(int myUcode, int friendUcode) {
        mapper.deleteFriend(myUcode, friendUcode);
    }

}