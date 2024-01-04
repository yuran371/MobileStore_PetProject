import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import entity.Country;
import entity.Gender;
import entity.RegistrationParams;
import lombok.SneakyThrows;

/**
 * 
 * Use only for test purposes.
 * 
 * Instance of this class send post request on {@code RegistrationServlet.java}
 * and try to create new User. Response and user data save in
 * {@link src/test/resources}.
 * 
 * 
 */

public class UserCreator extends Thread {

	private static final Random RANDOM = new Random();
	private final Writer OUTPUT_WRITER;

	public UserCreator(Writer userDetails) {
		OUTPUT_WRITER = userDetails;
	}

	@Override
	public void run() {
		init();
	}

	@SneakyThrows
	private void init() {
		String email = getSaltString() + "@yandex.ru";
		String password = getSaltString();
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			ClassicHttpRequest httpPost = ClassicRequestBuilder.post("http://localhost:8081/registration")
					.setEntity(createEntity(email, password)).build();
			httpClient.execute(httpPost, response -> {
				HttpEntity entity = response.getEntity();
				Path path = Path.of("src", "test", "resources", "response_files", currentThread().getName() + ".txt");
				Files.createDirectories(path.getParent());
				try (InputStream stream = entity.getContent();
						OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE,
								StandardOpenOption.APPEND)) {
					int oneByte = 0;
					while ((oneByte = stream.read()) != -1) {
						outputStream.write(oneByte);
					}
				}
				EntityUtils.consume(entity);
				return null;
			});
			synchronized (OUTPUT_WRITER) {
				OUTPUT_WRITER.write(email);
				OUTPUT_WRITER.write(password);
			}
		}

	}

	private UrlEncodedFormEntity createEntity(String email, String password) {
		String address = getAddressRandom();
		return new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair(RegistrationParams.EMAIL.label, email),
				new BasicNameValuePair(RegistrationParams.PASSWORD.label, password),
				new BasicNameValuePair(RegistrationParams.NAME.label, getSaltString()),
				new BasicNameValuePair(RegistrationParams.SURNAME.label, getSaltString()),
				new BasicNameValuePair(RegistrationParams.BIRTHDAY.label, getDateRandom()),
				new BasicNameValuePair(RegistrationParams.COUNTRY.label, getCountryRandom()),
				new BasicNameValuePair(RegistrationParams.CITY.label, getCity(address)),
				new BasicNameValuePair(RegistrationParams.ADDRESS.label, address),
				new BasicNameValuePair(RegistrationParams.PHONE_NUMBER.label, getPhoneRandom()),
				new BasicNameValuePair(RegistrationParams.GENDER.label, getGenderRandom())));
	}

	protected String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		while (salt.length() < 10) {
			int index = (int) (RANDOM.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	private static String getCountryRandom() {
		Country[] countries = Country.values();
		return countries[RANDOM.nextInt(countries.length)].name();
	}

	private static String getGenderRandom() {
		Gender[] gender = Gender.values();
		return gender[RANDOM.nextInt(gender.length)].name();
	}

	private static String getDateRandom() {
		long randomDayOfEpoch = RANDOM.nextLong(0L, LocalDate.now().minusYears(5).toEpochDay());
		LocalDate dateOfBirth = LocalDate.ofEpochDay(randomDayOfEpoch);
		return String.format("%02d.%02d.%d", dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue(),
				dateOfBirth.getYear());
	}

	@SneakyThrows
	private static String getAddressRandom() {
		Path path = Path.of("src", "test", "resources", "RU.txt");
		String result = "";
		int addressLineFromFile = RANDOM.nextInt(2000);
		try (Scanner scanner = new Scanner(path)) {
			long counter = 0L;
			while (scanner.hasNextLine()) {
				if (counter == addressLineFromFile) {
					result = scanner.nextLine();
				}
				scanner.nextLine();
				counter++;
			}
		}
		String address = result.replaceAll("[\t]", " ").replaceAll("[ ]{2,}", " ");
		return address;
	}

	private static String getCity(String address) {
		String string = address.split(" ")[2];
		return string;
	}

	private static String getPhoneRandom() {
		StringBuilder phoneNumber = new StringBuilder("+7");
		while (phoneNumber.length() < 12) { // length of the random string.
			phoneNumber.append(RANDOM.nextInt(10));
		}
		return phoneNumber.toString();
	}

}
