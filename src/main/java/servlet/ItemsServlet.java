package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import dto.CreateAccountDto;
import dto.ReadUserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ItemsService;
import utlis.JspHelper;

@WebServlet("/items")
public class ItemsServlet extends HttpServlet {

	private final ItemsService instanceService = ItemsService.getInstance();
	private final static String AUTHORIZATION_STATUS = "AuthorizationStatus";
	private final static String USER = "User";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

		if (req.getSession().getAttribute(USER) == null) {
			resp.sendRedirect("/registration");
			return;
		}

		req.getRequestDispatcher(JspHelper.getUrl("items")).forward(req, resp);

//		==================================

		var session = req.getSession();
		var userDto = (ReadUserDto) session.getAttribute(USER);
		try (PrintWriter printWriter = resp.getWriter()) {
			if (userDto != null && !session.isNew()) {
				printWriter.write("""
						<h1>Welcome, %s. You are authorized. </h1>
						""".formatted(userDto.getName()));
			}
			printWriter.write("<h2>Список телефонов</h2>");
			printWriter.write("<ul>");
			instanceService.findAllItems().forEach(item -> {
				printWriter.write("""
						<li>
							<a href="/items-parameters?itemId=%d"> %s %s %,.2f %s </a>
						</li>
						""".formatted(item.getItemId(), item.getBrand(), item.getModel(), item.getPrice(),
						item.getCurrency()));
			});
			printWriter.write("</ul>");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		{
			resp.setContentType("text/html");
			resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
			var session = req.getSession();
			var userDto = (CreateAccountDto) session.getAttribute(USER);
			try (PrintWriter printWriter = resp.getWriter()) {
				if (userDto != null && !session.isNew()) {
					printWriter.write("""
							<h1>Welcome, %s. You are authorized. </h1>
							""".formatted(userDto.getName()));
				}
				printWriter.write("<h1>Список телефонов</h1>");
				printWriter.write("<ul>");
				instanceService.findAllItems().forEach(item -> {
					printWriter.write("""
							<li>
								<a href="/items-parameters?itemId=%d"> %s %s %,.2f %s </a>
							</li>
							""".formatted(item.getItemId(), item.getBrand(), item.getModel(), item.getPrice(),
							item.getCurrency()));
				});
				printWriter.write("</ul>");
			}
		}
	}

}