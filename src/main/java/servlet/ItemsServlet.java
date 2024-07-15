package servlet;

import dto.filter.AttributesFilter;
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
    private ItemsService itemsService;
    public static final String AUTHORIZATION_STATUS = "AuthorizationStatus";
    public static final String USER = "User";
    public static final String ITEMS = "items";
    public static final String BRAND = "brand";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html");
		req.setAttribute(ITEMS, itemsService.findItemsWithParameters(AttributesFilter.builder().build(), 2, 3));
        req.getRequestDispatcher(JspHelper.getUrl("items"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        {
            resp.setContentType("text/html");
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String submitParameter = req.getParameter(BRAND);

//				List<OldItemsFilterDto> brand = oldItemsService.findBrand(submitParameter);
//				req.setAttribute(ITEMS, brand);

            req.getRequestDispatcher(JspHelper.getUrl("items"))
                    .forward(req, resp);
        }
    }

}