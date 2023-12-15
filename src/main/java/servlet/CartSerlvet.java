package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import dao.ItemsDao;
import dto.CartDto;
import dto.DtoPersonalAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import service.ItemFindByIdService;

@WebServlet("/items-cart")
public class CartSerlvet extends HttpServlet {

	private final static String USER = "User";
	private final static String CART = "Cart";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO fix adding same amount of items in cart when using refresh on browser
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var itemIdFromReq = Long.valueOf(req.getParameter("itemId"));
		var quantityFromRequest = Integer.valueOf(req.getParameter("quantityInCart"));
		@Cleanup
		var writer = resp.getWriter();
		int quantityCheck = quantityFromRequest != null ? quantityFromRequest : 0;
		var session = req.getSession();
		var user = (DtoPersonalAccount) session.getAttribute(USER);
		var cart = (CartDto) session.getAttribute(CART);
		ConcurrentHashMap<Long, Integer> mapOfItems = null;
		if (session.isNew() || user == null) {
			writer.write("""
					<h1>ываппYour are not authorized. Please create account before using cart. </h1>
					<a href = \"/login.html\">Create account</a>
					""");
			return;
		} else if (cart == null) {
			mapOfItems = new ConcurrentHashMap<Long, Integer>();
			mapOfItems.put(itemIdFromReq, quantityFromRequest);
			var cartDto = new CartDto(mapOfItems, user.email());
			session.setAttribute(CART, cartDto);
		} else {
			mapOfItems = cart.getItemsIdAndQuantity();
			if (mapOfItems.containsKey(itemIdFromReq)) {
				var quantityToAdd = mapOfItems.get(itemIdFromReq) + quantityFromRequest;
				Integer quantityFromDB = ItemsDao.getInstance().getByItemId(itemIdFromReq).get().getQuantity();
				if (quantityToAdd > quantityFromDB) {
					writer.write("""
							<h1> Quantity is invalid. You can add only %d </h1>
							<a href = \"/items-parameters?itemId=%d\">Return to item</a>
							""".formatted(quantityFromDB - mapOfItems.get(itemIdFromReq), itemIdFromReq));
					return;
				}
				mapOfItems.put(itemIdFromReq, quantityToAdd);
			} else {
				mapOfItems.put(itemIdFromReq, quantityFromRequest);
			}
		}
		writer.write("<h1> %s's cart </h1>".formatted(user.name() + user.surname()));
		ItemFindByIdService itemFindByIdService = ItemFindByIdService.getInstance();
		for (var entry : mapOfItems.entrySet()) {
			writer.write("<li> " + itemFindByIdService.findById(entry.getKey()).model() + " " + entry.getValue() + "шт."
					+ " </li>");
		}
	}
}
