import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class UserLoadTest {

	public static void main(String[] args) throws IOException {

		Path path = Path.of("src", "test", "resources", "users.txt");
		BufferedWriter newBufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
		List<UserCreator> pool = new ArrayList<>();
		int i = 0;
		while (i < 1) {
			pool.add(new UserCreator(newBufferedWriter));
			i++;
		}
		pool.stream().forEach(user -> user.start());
	}
}
