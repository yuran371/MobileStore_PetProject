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

@WebServlet("/items-add-to-cart")
public class AddToCartSerlvet extends HttpServlet {

	private final static String USER = "User";
	private final static String CART = "Cart";
	private final static String CART_STATUS = "CartStatus";

	public static enum CartStatus {
		NOT_AUTHORIZED, INVALID_QUANTITY, HAVE_ITEMS
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("text/html");
		var itemIdFromReq = Long.valueOf(req.getParameter("itemId"));
		var quantityFromRequest = Integer.valueOf(req.getParameter("quantityInCart"));
		var session = req.getSession();
		var user = (DtoPersonalAccount) session.getAttribute(USER);
		var cart = (CartDto) session.getAttribute(CART);
		ConcurrentHashMap<Long, Integer> mapOfItems = null;
		if (session.isNew() || user == null) {
			session.setAttribute(CART_STATUS, CartStatus.NOT_AUTHORIZED);
			resp.sendRedirect("/cart");
			return;
		} else if (cart == null) {
			mapOfItems = new ConcurrentHashMap<Long, Integer>();
			mapOfItems.put(itemIdFromReq, quantityFromRequest);
			var cartDto = new CartDto(mapOfItems, user.email());
			session.setAttribute(CART, cartDto);
			session.setAttribute(CART_STATUS, CartStatus.HAVE_ITEMS);
			resp.sendRedirect("/cart?email=%s".formatted(user.email()));
			return;
		} else {
			mapOfItems = cart.getItemsIdAndQuantity();
			if (mapOfItems.containsKey(itemIdFromReq)) {
				var quantityToAdd = mapOfItems.get(itemIdFromReq) + quantityFromRequest;
				Integer quantityFromDB = ItemsDao.getInstance().getByItemId(itemIdFromReq).get().getQuantity();
				if (quantityToAdd > quantityFromDB) {
					session.setAttribute(CART_STATUS, CartStatus.INVALID_QUANTITY);
					resp.sendRedirect("/cart?email=%s&canAdd=%d&itemId=%d".formatted(user.email(),
							quantityFromDB - mapOfItems.get(itemIdFromReq), itemIdFromReq));
					return;
				}
				mapOfItems.put(itemIdFromReq, quantityToAdd);
			} else {
				mapOfItems.put(itemIdFromReq, quantityFromRequest);
			}

			resp.sendRedirect("/cart?email=%s".formatted(user.email()));
			return;
		}
	}
}
