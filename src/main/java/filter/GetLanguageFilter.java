package filter;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class GetLanguageFilter extends HttpFilter {

	static {
		Locale.setDefault(new Locale("en"));
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		if (session.getAttribute("language") == null) {
			Locale userLocale = req.getLocale();
			session.setAttribute("language", userLocale.getLanguage());
		}
		chain.doFilter(req, res);
	}
}
