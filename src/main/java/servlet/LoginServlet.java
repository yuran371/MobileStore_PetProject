package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import dto.DtoPersonalAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PersonalAccountService;
import utlis.JspHelper;

@WebServlet("/registration")
public class LoginServlet extends HttpServlet {

	private final static String USER = "User";
	private final static String AUTHORIZATION_STATUS = "AuthorizationStatus";
	private final static String LOCAL_COOKIE = "LocalInfo";

	public static enum Country {
		RUSSIA, KAZAKHSTAN, UKRAINE, BELARUS
	}

	public static enum Gender {
		MALE, FEMALE
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		req.setAttribute("countries", Country.values());
		req.setAttribute("genders", Gender.values());
		req.getRequestDispatcher(JspHelper.getUrl("registration")).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var cookies = req.getCookies();
		boolean cookieIsEmpty = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(LOCAL_COOKIE))
				.findFirst().isEmpty() || cookies == null;
		if (cookieIsEmpty) {
			Cookie localCookie = new Cookie(LOCAL_COOKIE, req.getLocale().toString());
			resp.addCookie(localCookie);
		}
		var session = req.getSession();
		try (var writer = resp.getWriter()) {
			var user = (DtoPersonalAccount) session.getAttribute(USER);
			if (session.isNew() || user == null) {
				var email = req.getParameter("email");
				var name = req.getParameter("name");
				var surname = req.getParameter("surname");
				var country = req.getParameter("country");
				var city = req.getParameter("city");
				var address = req.getParameter("address");
				var phoneNumber = req.getParameter("phoneNumber");
				var dtoPersonalAccount = new DtoPersonalAccount(null, email, name, surname, country, city, address,
						phoneNumber);
				Optional<Long> addAccountResult = PersonalAccountService.getInctance().addAccount(dtoPersonalAccount);
				if (addAccountResult.isPresent()) {
					addAccountResult.ifPresent(presented -> session.setAttribute(USER, dtoPersonalAccount));
					req.setAttribute(AUTHORIZATION_STATUS, Boolean.TRUE);
					req.getRequestDispatcher("/items").forward(req, resp);
				} else {
					writer.write("<h1>Wrong login or phone number. Account is not created</h1>");
					writer.write("<a href = \"/login.html\">Go back to login</a>");
				}
//				addAccountResult.ifPresentOrElse(value -> {
//					writer.write("""
//							<h1>Welcome %s. Your account successfully added. </h1>
//							""".formatted(name));
//					writer.write("<a href = \"/login.html\">Create another account</a>");
//				}, () -> {
//					writer.write("<h1>Wrong login or phone number. Account is not created</h1>");
//					writer.write("<a href = \"/login.html\">Go back to login</a>");
//				});
//				if (addAccountResult.isPresent()) {
//					session.setAttribute(USER, dtoPersonalAccount);
//				}

			} else {
				resp.sendRedirect("/items");
			}
		}
	}

}
