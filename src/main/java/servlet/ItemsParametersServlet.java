package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import dto.ItemsFindByIdDto;
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
			ItemsFindByIdDto findByIdDto = instanceService.findById(itemIdd);
			writer.write("""
					<li>
						%s %s %s %,.2f %s %d шт.
					</li>
					""".formatted(findByIdDto.model(), findByIdDto.brand(), findByIdDto.attributes(),
					findByIdDto.price(), findByIdDto.currency(), findByIdDto.quantity()));
			writer.write("</ul>");
			writer.write("""
					<form action="items-cart?itemId=%d" method="post">
					Quantity:<input type="number" min="1" max="%d" name="quantityInCart" required/><br /><br />
					<input type="submit" value="Add to cart" />
					</form>
					""".formatted(itemIdd, findByIdDto.quantity()));
		}
	}
}
