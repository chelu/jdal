package info.joseluismartin.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet 2.3 Filter  for simple session auth
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class AuthFilter extends OncePerRequestFilter {
	
	public static String SESSION_USER_KEY = "AUTHFILTER_USER";
	
	private static String loginPage="/login.jsp";

	
	public void destroy() {

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		if (!isLoginPage(req) && req.getSession().getAttribute(SESSION_USER_KEY) == null) {
			// not authorized
			res.sendRedirect(req.getContextPath() + "/" + loginPage);
		}
		else {
			// authorized
			chain.doFilter(request, response);
		}
		
	}
	
	protected boolean isLoginPage(HttpServletRequest request) {
		return  ("/login.jsp".equals(request.getServletPath()) || 
				 "/login.do".equals(request.getServletPath()));
	}

}
