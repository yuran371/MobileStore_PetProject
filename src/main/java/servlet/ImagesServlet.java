package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;

@WebServlet(ImagesServlet.URL)
public class ImagesServlet extends HttpServlet {

	public static final String URL = "/images/*";
	@Serial
	private static final long serialVersionUID = 5049507779194083231L;
	private static final ImageService imageService = ImageService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageURI = req.getRequestURI().replace("/images", "");
		imageService.get(imageURI).ifPresentOrElse(image -> {
			resp.setContentType("application/octet-stream");
			writeImage(image, resp);
		}, () -> resp.setStatus(404));
	}

	@SneakyThrows
	private void writeImage(InputStream image, HttpServletResponse resp) {
		try (ServletOutputStream outputStream = resp.getOutputStream(); image) {
			int oneByte;
			while ((oneByte = image.read()) != -1) {
				outputStream.write(oneByte);
			}
		}
	}
}