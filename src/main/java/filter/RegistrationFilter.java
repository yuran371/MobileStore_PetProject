package filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(servletNames = "cart")
public class RegistrationFilter extends HttpFilter{
	
	private final static String USER = "User";
	
	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		if (session.getAttribute(USER)==null) {
			res.sendRedirect("/registration");
		} else {
			chain.doFilter(req, res);
		}
	}
}
