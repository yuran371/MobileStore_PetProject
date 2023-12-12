package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import dto.DtoPersonalAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AddLoginService;

@WebServlet("/loginResult")
public class loginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var username = req.getParameter("username");
		var fullName = req.getParameter("fullName");
		var country = req.getParameter("country");
		var city = req.getParameter("city");
		var address = req.getParameter("address");
		var phoneNumber = req.getParameter("phoneNumber");
		boolean addAccountResult = AddLoginService.getInctance()
				.addAccount(new DtoPersonalAccount(username, fullName, country, city, address, phoneNumber));
		try (var writer = resp.getWriter()) {
			if (addAccountResult) {
				writer.write("""
						<h1>Welcome %s. Your account successfully added. </h1>
						""".formatted(username));
				writer.write("<a href = \"/login.html\">Create another account</a>");
			} else {
				writer.write("<h1>Wrong login or phone number. Account is not created</h1>");
				writer.write("<a href = \"/login.html\">Go back to login</a>");
			}
		}

	}
}
