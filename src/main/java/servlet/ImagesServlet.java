package servlet;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import service.ImageService;

@WebServlet("/images/*")
public class ImagesServlet extends HttpServlet {

	ImageService imageService = ImageService.getInstance();

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