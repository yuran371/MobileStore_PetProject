package filter;

import static utlis.ServletURIs.*;
import java.io.IOException;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.LoginServlet;

@WebFilter(value = "/*")
public class AuthorizationFilter extends HttpFilter{
	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String requestURI = req.getRequestURI();
		HttpSession session = req.getSession();
		if(privateURI(requestURI) && notAuthorized(session)) {
			res.sendRedirect(LOGIN);
		} else {
			chain.doFilter(req, res);
		}
	}

	private boolean privateURI(String requestURI) {
		 Set<String> set = Set.of(CART, LOGOUT); 
		 return set.stream().anyMatch(uri -> requestURI.startsWith(uri));
	}
	
	private boolean notAuthorized(HttpSession session) {
		return session.getAttribute(LoginServlet.USER) == null ? true : false;
	}
	
}
