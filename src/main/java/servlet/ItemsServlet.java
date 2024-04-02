package servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ItemsService;
import utlis.JspHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/items")
public class ItemsServlet extends HttpServlet {

    @Inject
    private ItemsService ItemsService;
    private final static String AUTHORIZATION_STATUS = "AuthorizationStatus";
    private final static String USER = "User";
    private final static String ITEMS = "items";
    private final static String BRAND = "brand";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html");
//		req.setAttribute(ITEMS, ItemsService.findItemsWithParameters(AttributesFilter.builder().build(), 2, 3));
        req.getRequestDispatcher(JspHelper.getUrl("items"))
                .forward(req, resp);
//		System.out.println("privet");
//		@Cleanup PrintWriter printWriter = resp.getWriter();
//		printWriter.write("""
//						<h1>Welcome, %s. You are authorized. </h1>
//						""");

//		==================================

//		var session = req.getSession();
//		var userDto = (ReadUserDto) session.getAttribute(USER);
//		try (PrintWriter printWriter = resp.getWriter()) {
//			if (userDto != null && !session.isNew()) {
//				printWriter.write("""
//						<h1>Welcome, %s. You are authorized. </h1>
//						""".formatted(userDto.getName()));
//			}
//			printWriter.write("<h2>Список телефонов</h2>");
//			printWriter.write("<ul>");
//			itemsService.findAllItems().forEach(item -> {
//				printWriter.write("""
//						<li>
//							<a href="/items-parameters?itemId=%d"> %s %s %,.2f %s </a>
//						</li>
//						""".formatted(item.getItemId(), item.getBrand(), item.getModel(), item.getPrice(),
//						item.getCurrency()));
//			});
//			printWriter.write("</ul>");
//		}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        {
            resp.setContentType("text/html");
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String submitParameter = req.getParameter(BRAND);
            if (submitParameter.equals("AllPhones")) {
//				req.setAttribute(ITEMS, ItemsService.findAllItems());
            } else {
//				List<OldItemsFilterDto> brand = oldItemsService.findBrand(submitParameter);
//				req.setAttribute(ITEMS, brand);
            }
            req.getRequestDispatcher(JspHelper.getUrl("items"))
                    .forward(req, resp);
        }
    }

}