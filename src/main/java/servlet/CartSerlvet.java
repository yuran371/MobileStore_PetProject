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

@WebServlet("/items-cart")
public class CartSerlvet extends HttpServlet {

	private final static String USER = "User";
	private final static String CART = "Cart";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var itemIdFromReq = Long.valueOf(req.getParameter("itemId"));
		var quantityFromRequest = Integer.valueOf(req.getParameter("quantityInCart"));
		@Cleanup
		var writer = resp.getWriter();
		int quantityCheck = quantityFromRequest != null ? quantityFromRequest : 0;
		if (quantityCheck > ItemsDao.getInstance().getByItemId(itemIdFromReq).get().getQuantity()) {
			// TODO Add html for invalid quantity
			writer.write("""
					<h1>Your are not authorized. Please create account before using cart. </h1>
					<a href = \"/login.html\">Create account</a>
					""");
			return;
		}
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		var session = req.getSession();
		var user = (DtoPersonalAccount) session.getAttribute(USER);
		var cart = (CartDto) session.getAttribute(CART);
		ConcurrentHashMap<Long, Integer> mapOfItems;
		if (session.isNew() || user.equals(null)) {
			writer.write("""
					<h1>Your are not authorized. Please create account before using cart. </h1>
					<a href = \"/login.html\">Create account</a>
					""");
		} else if (cart.equals(null)) {
			mapOfItems = new ConcurrentHashMap<Long, Integer>();
			mapOfItems.put(itemIdFromReq, quantityFromRequest);
			var cartDto = new CartDto(mapOfItems, user.email());
			session.setAttribute(CART, cartDto);
		} else {
			mapOfItems = cart.getItemsIdAndQuantity();
			if (mapOfItems.containsKey(itemIdFromReq)) {
				var quantityFromCart = mapOfItems.get(itemIdFromReq);
				mapOfItems.put(itemIdFromReq, quantityFromCart + quantityFromRequest);
			} else {
				mapOfItems.put(itemIdFromReq, quantityFromRequest);
			}
			// TODO Create HTML with all items in cart
		}
	}
}
