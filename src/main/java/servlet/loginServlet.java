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

@WebServlet("/loginResult")
public class loginServlet extends HttpServlet {

	private final static String AUTORIZATION_CHECK = "loginCheck";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getCookies();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var cookies = req.getCookies();
		try (var writer = resp.getWriter()) {
			if (cookies == null || (Arrays.stream(cookies).map(cookie -> cookie.getName().equals(AUTORIZATION_CHECK))
					.findFirst().isEmpty())) {
				var email = req.getParameter("email");
				var name = req.getParameter("name");
				var surname = req.getParameter("surname");
				var country = req.getParameter("country");
				var city = req.getParameter("city");
				var address = req.getParameter("address");
				var phoneNumber = req.getParameter("phoneNumber");
				Optional<Long> addAccountResult = PersonalAccountService.getInctance()
						.addAccount(new DtoPersonalAccount(email, name, surname, country, city, address, phoneNumber));
				if (addAccountResult.isPresent()) {
					writer.write("""
							<h1>Welcome %s. Your account successfully added. </h1>
							""".formatted(name));
					writer.write("<a href = \"/login.html\">Create another account</a>");
				} else {
					writer.write("<h1>Wrong login or phone number. Account is not created</h1>");
					writer.write("<a href = \"/login.html\">Go back to login</a>");
				}
				if (addAccountResult.isPresent()) {
					var cookie = new Cookie(AUTORIZATION_CHECK, addAccountResult.get().toString());
					cookie.setPath("/loginResult");
					cookie.setMaxAge(60 * 60);
					resp.addCookie(cookie);
				}
			} else {
				Cookie authorizationCookie = Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals(AUTORIZATION_CHECK)).findFirst().get();
				DtoPersonalAccount byId = PersonalAccountService.getInctance()
						.getById(Long.parseLong(authorizationCookie.getValue()));
				writer.write("""
						<h1>Welcome %s. Your are already authorized. </h1>
						""".formatted(byId.email()));
			}
		}
	}

}
