package dao;

import java.util.List;
import model.User;

public interface UserMapper {
	
	int InsertUser(User user);
	
	User selectUserByUno(int uno);
	
	int checkId(String id);
	
	List<User> selectAllUsers();
	
	int updateUser(User user);
	
	int deleteUser(long id);
}
