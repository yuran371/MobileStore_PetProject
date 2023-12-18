package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import dto.CartDto;
import dto.DtoPersonalAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import service.ItemFindByIdService;
import servlet.AddToCartSerlvet.CartStatus;

@WebServlet("/cart")
public class CartSerlvet extends HttpServlet {

	private final static String CART_STATUS = "CartStatus";
	private final static String USER = "User";
	private final static String CART = "Cart";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var session = req.getSession();
		resp.setContentType("text/html");
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		@Cleanup
		var writer = resp.getWriter();
		var cartStatus = (CartStatus) session.getAttribute(CART_STATUS);
		var userEmail = (String) req.getParameter("email");
		if (userEmail == null || cartStatus == null || cartStatus == CartStatus.NOT_AUTHORIZED) {
			writer.write("""
					<h1>Your are not authorized. Please create account before using cart. </h1>
					<a href = \"/login.html\">Create account</a>
					""");
			return;
		}
		switch (cartStatus) {
		case INVALID_QUANTITY -> {
			writer.write("""
					<h1> Quantity is invalid. You can add only %s </h1>
					<a href = \"/items-parameters?itemId=%s\">Return to item</a>
					""".formatted(req.getParameter("canAdd"), req.getParameter("itemId")));
			return;
		}
		case HAVE_ITEMS -> {
			var user = (DtoPersonalAccount) session.getAttribute(USER);
			var cart = (CartDto) session.getAttribute(CART);
			var mapOfItems = cart.getItemsIdAndQuantity();
			var name = user.name();
			writer.write("<h1> %s's cart </h1>".formatted(name + user.surname()));
			ItemFindByIdService itemFindByIdService = ItemFindByIdService.getInstance();
			for (var entry : mapOfItems.entrySet()) {
				writer.write("<li> " + itemFindByIdService.findById(entry.getKey()).model() + " " + entry.getValue()
						+ "шт." + " </li>");
			}
		}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CartSerlvet servlet = new CartSerlvet();
		servlet.doGet(req, resp);
	}
}