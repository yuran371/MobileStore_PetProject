package servlet;

import dto.personalAccount.AuthUserDto;
import dto.personalAccount.ReadUserInfoDto;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.validation.ConstraintViolation;
import service.PersonalAccountService;
import utlis.JspHelper;
import utlis.TokenHandler;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@WebServlet(LoginServlet.URL)
public class LoginServlet extends HttpServlet {

    public static final String URL = "/login";
    public static final String USER = "User";
    public static final String TOKEN_COOKIE = "auth";
    private static final String AUTHORIZATION_ERRORS = "error";
    private static final int COOKIE_EXPIRY_TIME = 60 * 60;
    @Serial
    private static final long serialVersionUID = -618441551049690046L;
    @Inject
    private PersonalAccountService personalAccountService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getUrl("login")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute(USER) != null) {
            req.getRequestDispatcher(JspHelper.getUrl("items")).forward(req, resp);
            return;
        }
        AuthUserDto authUserDto = AuthUserDto.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> authResult =
                personalAccountService.authUser(authUserDto);
        if (authResult.isRight()) {
            req.setAttribute(AUTHORIZATION_ERRORS, authResult.get());
            doGet(req, resp);
            return;
        }
        Optional<ReadUserInfoDto> readUserInfoDto = authResult.getLeft();
        if (readUserInfoDto.isEmpty()) {
            req.setAttribute(AUTHORIZATION_ERRORS, "not a user");
            return;
        }
        session.setAttribute(USER, readUserInfoDto.get());
        var cookies = req.getCookies();
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie != null && cookie.getName().equals(TOKEN_COOKIE))
                .findFirst();
        jwtCookie.ifPresentOrElse(cookie -> cookie.setValue(TokenHandler.generateToken(authUserDto.getEmail())),
                                  () -> {
                                      Cookie cookie = new Cookie(TOKEN_COOKIE,
                                                                 TokenHandler.generateToken(authUserDto.getEmail()));
                                      cookie.setMaxAge(COOKIE_EXPIRY_TIME);
                                      resp.addCookie(cookie);
                                  });
        req.getRequestDispatcher(JspHelper.getUrl("items")).forward(req, resp);
    }
}
