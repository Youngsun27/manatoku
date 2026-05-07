package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/user/*")
public class SessionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		//캐시 방지
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);
		
		//로그인 체크
		HttpSession session = req.getSession(false);
		String loginId = (session == null) ? null : (String) session.getAttribute("loginId");
		if(loginId != null) {
			
			res.sendRedirect(req.getContextPath() + "/main/mainpage.jsp");
            return;
		}
		
		chain.doFilter(request, response);
		

	}
	@Override
	public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {}
	
	@Override
	public void destroy() {}
}
