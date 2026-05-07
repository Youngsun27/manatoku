package web;

import dao.UserMapper;
import model.User;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user-list-test")
public class UserListTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");

        try (SqlSession session = MyBatisUtil.getFactory().openSession()) { // select는 commit 필요 없음
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> list = mapper.selectAllUsers();

            resp.getWriter().println("총 " + list.size() + "건");
            for (User u : list) {
                resp.getWriter().println(u.getId() + " / " + u.getUsername() + " / " + u.getEmail());
            }
        }
    }
}