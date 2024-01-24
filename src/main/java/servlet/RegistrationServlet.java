package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

import dto.CreateAccountDto;
import entity.Country;
import entity.Gender;
import io.vavr.control.Either;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CreateAccountService;
import utlis.JspHelper;
import validator.ValidationErrors;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

	private final static String USER = "User";
	private final static String AUTHORIZATION_STATUS = "AuthorizationStatus";
	private final static String LOCAL_COOKIE = "LocalInfo";
	private final static String AUTHORIZATION_ERRORS = "errors";

	CreateAccountService createAccountService = CreateAccountService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		if (req.getSession().getAttribute(USER) != null) {
			resp.sendRedirect("/items");
			return;
		}

		req.setAttribute("countries", Country.values());
		req.setAttribute("genders", Gender.values());
		req.getRequestDispatcher(JspHelper.getUrl("registration")).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var cookies = req.getCookies();
		boolean cookieIsEmpty = cookies == null
				|| Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(LOCAL_COOKIE)).findFirst().isEmpty();
		if (cookieIsEmpty) {
			Cookie localCookie = new Cookie(LOCAL_COOKIE, req.getLocale().toString());
			resp.addCookie(localCookie);
		}
		Enumeration<String> parameterNames = req.getParameterNames();
		var session = req.getSession();
		CreateAccountDto accountDto = CreateAccountDto.builder().email(req.getParameter("email"))
				.password(req.getParameter("password")).name(req.getParameter("name"))
				.surname(req.getParameter("surname")).image(req.getPart("image"))
				.birthday(req.getParameter("dateOfBirth")).country(req.getParameter("country"))
				.city(req.getParameter("city")).address(req.getParameter("address"))
				.phoneNumber(req.getParameter("phoneNumber")).gender(req.getParameter("gender")).build();
		Either<Optional<Long>, ValidationErrors> save = createAccountService.save(accountDto);
		if (save.isRight()) {
			req.setAttribute(AUTHORIZATION_ERRORS, save.get().getCreateAccountErrors());
			doGet(req, resp);
			return;
		}
		resp.sendRedirect("/login");
		return;
	}
}
