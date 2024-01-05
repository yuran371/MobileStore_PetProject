package mapper;

import dto.CreateAccountDto;
import entity.Country;
import entity.Gender;
import entity.PersonalAccountEntity;
import jakarta.servlet.http.Part;
import utlis.DateFormatter;

public class CreateAccountMapper {

	private static CreateAccountMapper INSTANCE = new CreateAccountMapper();
	private static final String USER_FOLDER = "user\\";
	private static final String DEFAULT_AVATAR = "user\\default-avatar-icon-of-social-media-user-vector.jpg";

	private CreateAccountMapper() {
	}

	public static CreateAccountMapper getInstance() {
		return INSTANCE;
	}

	public PersonalAccountEntity mapOf(CreateAccountDto account) {

		Part image = account.getImage();
		PersonalAccountEntity personalAccountEntity = PersonalAccountEntity.builder().email(account.getEmail())
				.password(account.getPassword()).name(account.getName()).surname(account.getSurname())
				.image(USER_FOLDER + image == null ? "" : image.getSubmittedFileName())
				.birthday(DateFormatter.getDate(account.getBirthday())).country(Country.getValue(account.getCountry()))
				.city(account.getCity()).address(account.getAddress()).phoneNumber(account.getPhoneNumber())
				.gender(Gender.valueOf(account.getGender())).build();
		if (account.getImage().getSubmittedFileName().isBlank()) {
			personalAccountEntity.setImage(DEFAULT_AVATAR);
		}
		return personalAccountEntity;
	}
}
