package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.Optional;

@WebServlet(LogoutServlet.URL)
public class LogoutServlet extends HttpServlet {

	public static final String URL = "/logout";
	@Serial
	private static final long serialVersionUID = 409837276354212602L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.getSession().invalidate();
		Optional<Cookie> cookie1 = Arrays.stream(req.getCookies())
				.filter(cookie ->LoginServlet.TOKEN_COOKIE.equals(cookie.getName()))
				.findFirst();
		cookie1.ifPresent(cookie -> {
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
		});
		resp.sendRedirect(LoginServlet.URL);
	}
}
