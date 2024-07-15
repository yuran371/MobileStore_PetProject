package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import servlet.LoginServlet;
import utlis.TokenHandler;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(value = "/logout")
public class AuthFilter extends HttpFilter {
    @Serial
    private static final long serialVersionUID = 8561716989268761320L;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<Cookie> jwtTokenCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> LoginServlet.TOKEN_COOKIE.equals(cookie.getName()))
                .findFirst();
        boolean authenticated = jwtTokenCookie.map(cookie -> {
                    String value = cookie.getValue();
                    return TokenHandler.checkTokenAuthenticity(value);
                })
                .orElse(false);
        if (authenticated) {
            chain.doFilter(req, res);
        } else {
            res.sendRedirect(LoginServlet.URL);
        }
    }
}
