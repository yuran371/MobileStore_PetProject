package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utlis.JspHelper;

@WebServlet("/cart")
public class CartSerlvet extends HttpServlet {

	private final static String USER = "User";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var session = req.getSession();
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		if (session.getAttribute(USER) == null) {
			req.getRequestDispatcher(JspHelper.getUrl("notAuthorized")).forward(req, resp);
			return;
		}
		req.getRequestDispatcher(JspHelper.getUrl("cart")).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CartSerlvet servlet = new CartSerlvet();
		servlet.doGet(req, resp);
	}
}