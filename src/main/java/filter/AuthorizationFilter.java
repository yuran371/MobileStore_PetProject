package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.ImagesServlet;
import servlet.LoginServlet;

import java.io.IOException;
import java.io.Serial;
import java.util.Set;

@WebFilter(value = "/*")
public class AuthorizationFilter extends HttpFilter {
    @Serial
    private static final long serialVersionUID = 4963379131652527568L;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        if (privateURI(requestURI) && notAuthorized(session)) {
            res.sendRedirect(LoginServlet.URL);
        } else {
            chain.doFilter(req, res);
        }
    }

    private boolean privateURI(String requestURI) {
        Set<String> set = Set.of(ImagesServlet.URL);
        return set.stream()
                .anyMatch(requestURI::startsWith);
    }

    private boolean notAuthorized(HttpSession session) {
        return session.getAttribute(LoginServlet.USER) == null;
    }

}
