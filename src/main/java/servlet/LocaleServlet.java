package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/locale")
public class LocaleServlet extends HttpServlet{
	
	public final static String LANGUAGE = "language";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String parameter = req.getParameter(LANGUAGE);
		req.getSession().setAttribute(LANGUAGE, parameter);
		
		String headerReferer = req.getHeader("referer");
		resp.sendRedirect(headerReferer);
	}
}
