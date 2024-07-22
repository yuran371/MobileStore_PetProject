package service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import utlis.jdbc.PropertiesUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageService {

	private static final ImageService INSTANCE = new ImageService();
	private static final String BASE_PATH = PropertiesUtil.getProperty("image.base.path");
	private static final String DEFAULT_IMAGE = "default-avatar-icon-of-social-media-user-vector.jpg";

	public static ImageService getInstance() {
		return INSTANCE;
	}

	@SneakyThrows
	public void upload(String imagePath, InputStream stream) {
		if (imagePath.equals("user\\")) {
			Path path = Path.of(BASE_PATH, DEFAULT_IMAGE);
			return;
		}
		Path path = Path.of(BASE_PATH, imagePath);
		if (!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		try (stream) {
			Files.write(path, stream.readAllBytes(), CREATE, TRUNCATE_EXISTING);
		}
	}

	@SneakyThrows
	public Optional<InputStream> get(String URL) {
		Path imagePath = Path.of(BASE_PATH, URL);
		return Files.exists(imagePath) ? Optional.of(Files.newInputStream(imagePath)) : Optional.empty();
	}
}
