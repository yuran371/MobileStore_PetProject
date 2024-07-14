package servlet;

import dto.personalAccount.CreateAccountDto;
import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import service.PersonalAccountService;
import utlis.JspHelper;
import utlis.TokenHandler;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(RegistrationServlet.URL)
public class RegistrationServlet extends HttpServlet {

    public static final String URL = "/registration";
    @Serial
    private static final long serialVersionUID = 3920936508797910281L;
    private static final String USER = "User";
    private static final String CREATE_ACCOUNT_ERRORS = "errors";
    private static final String TOKEN_COOKIE = "auth";

    @Inject
    private PersonalAccountService personalAccountService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF_8.name());
        resp.setContentType("text/html");
        if (req.getSession().getAttribute(USER) != null) {
            resp.sendRedirect("/items");
            return;
        }
        req.setAttribute("countries", CountryEnum.values());
        req.setAttribute("genders", GenderEnum.values());
        req.getRequestDispatcher(JspHelper.getUrl("registration")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF_8.name());
        resp.setContentType("text/html");
        CreateAccountDto accountDto = CreateAccountDto.builder().email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .name(req.getParameter("name"))
                .surname(req.getParameter("surname"))
                .image(req.getPart("image"))
                .birthday(req.getParameter("dateOfBirth"))
                .country(req.getParameter("country"))
                .city(req.getParameter("city"))
                .address(req.getParameter("address"))
                .phoneNumber(req.getParameter("phoneNumber"))
                .gender(req.getParameter("gender"))
                .build();
        Either<Long, Set<? extends ConstraintViolation<?>>> save = personalAccountService.createUser(accountDto);
        if (save.isRight()) {
            req.setAttribute(CREATE_ACCOUNT_ERRORS, save.get());
            this.doGet(req, resp);
            return;
        }
        var cookies = req.getCookies();
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie != null && cookie.getName().equals(TOKEN_COOKIE))
                .findFirst();
        jwtCookie.ifPresentOrElse(cookie -> cookie.setValue(TokenHandler.generateToken(accountDto.getEmail())),
                                  () -> {
                                      Cookie cookie = new Cookie(TOKEN_COOKIE,
                                                                 TokenHandler.generateToken(accountDto.getEmail()));
                                      resp.addCookie(cookie);
                                  });
        resp.sendRedirect("/login");
    }
}
