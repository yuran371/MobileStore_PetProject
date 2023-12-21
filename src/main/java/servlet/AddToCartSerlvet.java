package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import dto.CartDto;
import dto.CreateAccountDto;
import dto.ItemsDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ItemsService;
import utlis.JspHelper;

@WebServlet("/items-add-to-cart")
public class AddToCartSerlvet extends HttpServlet {

	private final static String USER = "User";
	private final static String CART = "Cart";
	private final static String CART_STATUS = "CartStatus";
	private final static ItemsService itemsService = ItemsService.getInstance();

	public static enum CartStatus {
		INVALID_QUANTITY, HAVE_ITEMS
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		var session = req.getSession();
		var user = (CreateAccountDto) session.getAttribute(USER);
		if (user == null) {
			req.getRequestDispatcher(JspHelper.getUrl("notAuthorized")).forward(req, resp);
			return;
		}
		var itemIdFromReq = Long.valueOf(req.getParameter("itemId"));
		var quantityFromRequest = Integer.valueOf(req.getParameter("quantityInCart"));
		var cart = (CartDto) session.getAttribute(CART);
		ConcurrentHashMap<Long, ItemsDto> mapOfItems = null;
		if (cart == null) {
			mapOfItems = new ConcurrentHashMap<Long, ItemsDto>();
			ItemsDto itemDto = itemsService.findById(itemIdFromReq);
			itemDto.setQuantity(quantityFromRequest);
			mapOfItems.put(itemIdFromReq, itemDto);
			session.setAttribute(CART, new CartDto(mapOfItems, user.email()));
			session.setAttribute(CART_STATUS, CartStatus.HAVE_ITEMS);
			resp.sendRedirect("/cart?email=%s".formatted(user.email()));
			return;
		} else {
			mapOfItems = cart.getItemsDtoMap();
			if (mapOfItems.containsKey(itemIdFromReq)) {
				ItemsDto itemFromCart = mapOfItems.get(itemIdFromReq);
				var quantityToAdd = itemFromCart.getQuantity() + quantityFromRequest;
				Integer quantityFromDB = itemsService.findById(itemIdFromReq).getQuantity();
				if (quantityToAdd > quantityFromDB) {
					session.setAttribute(CART_STATUS, CartStatus.INVALID_QUANTITY);
					resp.sendRedirect("/cart?email=%s&canAdd=%d&itemId=%d".formatted(user.email(),
							quantityFromDB - itemFromCart.getQuantity(), itemIdFromReq));
					return;
				}
				itemFromCart.setQuantity(quantityToAdd);
				mapOfItems.put(itemIdFromReq, itemFromCart);
			} else {
				ItemsDto itemDto = itemsService.findById(itemIdFromReq);
				itemDto.setQuantity(quantityFromRequest);
				mapOfItems.put(itemIdFromReq, itemDto);
			}
			session.setAttribute(CART_STATUS, CartStatus.HAVE_ITEMS);
			resp.sendRedirect("/cart?email=%s".formatted(user.email()));
			return;
		}
	}
}
