package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ItemFindByIdService;

@WebServlet("/items-parameters")
public class ItemsParametersServlet extends HttpServlet {

	private final ItemFindByIdService instanceService = ItemFindByIdService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		Long itemIdd = Long.valueOf(req.getParameter("itemId"));

		try (PrintWriter writer = resp.getWriter()) {
			writer.write("<h1>Параметры и количество телефонов</h1>");
			writer.write("<ul>");
			instanceService.itemsServiceMethod(itemIdd).forEach(dto -> {
				writer.write("""
						<li>
							%d %s %s %s %,.2f %s %d шт.
						</li>
						""".formatted(dto.itemId(), dto.model(), dto.brand(), dto.attributes(), dto.price(),
						dto.currency(), dto.quantity()));
			});
			writer.write("</ul>");
			writer.write("""
					<form action="items-cart?itemId=%d" method="post">
					Quantity:<input type="number" min="1" name="quantityInCart" /><br /><br />
					<input type="submit" value="Add to cart" />
					</form>
					""".formatted(itemIdd));
		}
	}
}
