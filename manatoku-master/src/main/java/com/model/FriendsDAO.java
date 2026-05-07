package com.model;


import java.sql.*;
import java.util.Vector;
import javax.sql.*;

import model.Member;

import javax.naming.*;

public class FriendsDAO {
        
        private Connection getConnection() {
                Connection con = null;
                
                try { //dbcp api로 연결
                        Context initContext = new InitialContext();
                        Context envContext  = (Context)initContext.lookup("java:/comp/env");
                        DataSource ds = (DataSource)envContext.lookup("jdbc/myOracle");
                        con = ds.getConnection();
                } catch (Exception e) {
                        System.out.println("Error : Connection 생성 실패!!!");
                }
                
                return con;
        }
        
        
        
         //멤버를 데이터베이스에서 검색해서 Vector에 저장해서 리턴해주는 메소드를 구현
        public Vector<Member> idRead(String id) {
                System.out.println("idRead() 호출됨, id = [" + id + "]");
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            Vector<Member> vecList = new Vector<>();

            try {
                con = getConnection();
                String sql = "select * from member where id like ?";
                pstmt = con.prepareStatement(sql);
                System.out.println("SQL 실행: " + sql);
                pstmt.setString(1, "%" + id + "%");

                rs = pstmt.executeQuery();

                while (rs.next()) {
                        System.out.println("조회 결과 있음!");
                    Member member = new Member();
                    member.setUcode(rs.getInt("ucode"));
                    member.setId(rs.getString("id"));
                    member.setName(rs.getString("name"));
                    member.setIcon(rs.getString("icon"));
                    member.setPass(rs.getString("pass"));
                    member.setBirth(rs.getString("birth"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));

                    vecList.add(member);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) {}
                try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
                try { if (con != null) con.close(); } catch (Exception e) {}
            }

            return vecList;
        } //end ucodeRead

    // 친구 신청 (중복 방지 + 거절 후 재신청 허용)
    public boolean sendFriendRequest(int sender, int receiver) {

        String checkSql =
          "SELECT status FROM friends " +
          "WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?)";

        String insertSql =
          "INSERT INTO friends VALUES (friends_seq.NEXTVAL, ?, ?, 'WAIT', SYSDATE)";

        try (Connection con = getConnection()) {

            PreparedStatement check = con.prepareStatement(checkSql);
            check.setInt(1, sender);
            check.setInt(2, receiver);
            check.setInt(3, receiver);
            check.setInt(4, sender);

            ResultSet rs = check.executeQuery();

            if (rs.next() && !"REJECT".equals(rs.getString("status"))) {
                return false;
            }

            PreparedStatement insert = con.prepareStatement(insertSql);
            insert.setInt(1, sender);
            insert.setInt(2, receiver);
            insert.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // 받은 친구 신청
    public Vector<FriendsTWO> getReceiveRequest(int myUcode) {

        Vector<FriendsTWO> list = new Vector<>();

        String sql =
          "SELECT f.fcode, m.ucode, m.id, m.name, m.icon " +
          "FROM friends f JOIN member m ON f.sender = m.ucode " +
          "WHERE f.receiver=? AND f.status='WAIT'";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, myUcode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	FriendsTWO f = new FriendsTWO();
                f.setFcode(rs.getInt("fcode"));
                f.setFriendUcode(rs.getInt("ucode"));
                f.setFriendId(rs.getString("id"));
                f.setFriendName(rs.getString("name"));
                f.setFriendIcon(rs.getString("icon"));
                list.add(f);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // 수락 / 거절
    public void updateFriendStatus(int fcode, String status) {

        String sql = "UPDATE friends SET status=? WHERE fcode=?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, fcode);
            pstmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    // 친구 목록
    public Vector<FriendsTWO> getFriendList(int myUcode) {

        Vector<FriendsTWO> list = new Vector<>();

        String sql =
          "SELECT m.ucode, m.id, m.name, m.icon " +
          "FROM friends f JOIN member m " +
          "ON (CASE WHEN f.sender=? THEN f.receiver ELSE f.sender END)=m.ucode " +
          "WHERE (f.sender=? OR f.receiver=?) AND f.status='ACCEPT'";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, myUcode);
            pstmt.setInt(2, myUcode);
            pstmt.setInt(3, myUcode);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	FriendsTWO f = new FriendsTWO();
                f.setFriendUcode(rs.getInt("ucode"));
                f.setFriendId(rs.getString("id"));
                f.setFriendName(rs.getString("name"));
                f.setFriendIcon(rs.getString("icon"));
                list.add(f);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // 친구 삭제
    public void deleteFriend(int myUcode, int friendUcode) {

        String sql =
          "DELETE FROM friends " +
          "WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, myUcode);
            pstmt.setInt(2, friendUcode);
            pstmt.setInt(3, friendUcode);
            pstmt.setInt(4, myUcode);
            pstmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
    
   
}