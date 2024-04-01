package servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/add-item")
public class AddItemServlet extends HttpServlet {
    @Inject
    private final service.ItemsService itemsService;

    public AddItemServlet(service.ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html");
        String brand = req.getParameter("brand");
        String color = req.getParameter("color");
        req.getParameter("");
//        itemsService.insert();
        super.doGet(req, resp);
    }
}
