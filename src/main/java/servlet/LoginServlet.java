package servlet;

import java.io.IOException;

import dto.LoginUserDto;
import dto.ReadUserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import service.LoginService;
import utlis.JspHelper;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private final LoginService loginService = LoginService.getInstance();
	public final static String USER = "User";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(JspHelper.getUrl("login")).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		LoginUserDto dto = LoginUserDto.builder().email(req.getParameter("email"))
				.password(req.getParameter("password")).build();
		loginService.checkUser(dto).ifPresentOrElse(user -> successLogin(user, session, req, resp),
				() -> loginError(req, resp));
	}

	@SneakyThrows
	private void loginError(HttpServletRequest req, HttpServletResponse resp) {
		resp.sendRedirect("/login?error");
	}

	@SneakyThrows
	private static void successLogin(ReadUserDto dto, HttpSession session, HttpServletRequest req,
			HttpServletResponse resp) {
		session.setAttribute(USER, dto);
		resp.sendRedirect("/items");
	}

}
