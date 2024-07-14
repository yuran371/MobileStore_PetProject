package servlet;

import dto.AddItemDto;
import entity.ItemsEntity;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import service.ItemsService;
import utlis.JspHelper;

import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

@WebServlet("/add-item")
public class AddItemServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -8492324644385946171L;
    @Inject
    private ItemsService itemsService;
    private static final String ITEMS_ERRORS = "errors";
    private static final String TOKEN_COOKIE = "auth";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html");
        req.getRequestDispatcher(JspHelper.getUrl("addItem"))
                .forward(req, resp);
        String brand = req.getParameter("brand");
        String color = req.getParameter("color");
        req.getParameter("");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF_8.name());
        resp.setContentType("text/html");
        AddItemDto itemDto = AddItemDto.builder()
                .model(req.getParameter("model"))
                .brand(req.getParameter("brand"))
                .ram(req.getParameter("ram"))
                .internalMemory(req.getParameter("internalMemory"))
                .os(req.getParameter("os"))
                .color(req.getParameter("color"))
                .price(req.getParameter("price"))
                .currency(req.getParameter("currency"))
                .quantity(req.getParameter("quantity"))
                .build();
        Either<Optional<ItemsEntity>, Set<? extends ConstraintViolation<?>>> save = itemsService.insert(itemDto);
        if (save.isRight()) {
            req.setAttribute(ITEMS_ERRORS, save.get());
            doGet(req, resp);
            return;
        }
        var session = req.getSession();
        var cookies = req.getCookies();
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie != null && cookie.getName().equals(TOKEN_COOKIE))
                .findFirst();
//        jwtCookie.ifPresentOrElse(cookie -> cookie.setValue(TokenHandler.generateToken(accountDto.getEmail())),
//                () -> {
//                    Cookie cookie = new Cookie(TOKEN_COOKIE,
//                            TokenHandler.generateToken(accountDto.getEmail()));
//                    resp.addCookie(cookie);
//                });
        resp.sendRedirect("/add-item");
    }

}
