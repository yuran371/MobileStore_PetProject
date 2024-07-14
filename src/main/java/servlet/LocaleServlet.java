package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet(LocaleServlet.URL)
public class LocaleServlet extends HttpServlet {

    public static final String LANGUAGE = "language";
    public static final String URL = "/locale";
    @Serial
    private static final long serialVersionUID = -6630991717887797512L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parameter = req.getParameter(LANGUAGE);
        req.getSession().setAttribute(LANGUAGE, parameter);

        String headerReferer = req.getHeader("referer");
        resp.sendRedirect(headerReferer);
    }
}
