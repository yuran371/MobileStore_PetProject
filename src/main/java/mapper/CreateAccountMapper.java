package mapper;

import dto.CreateAccountDto;
import entity.Country;
import entity.Gender;
import entity.PersonalAccountEntity;
import utlis.DateFormatter;

public class CreateAccountMapper {

	private static CreateAccountMapper INSTANCE = new CreateAccountMapper();

	private CreateAccountMapper() {
	}

	public static CreateAccountMapper getInstance() {
		return INSTANCE;
	}

	public PersonalAccountEntity mapOf(CreateAccountDto account) {
		return PersonalAccountEntity.builder().email(account.getEmail()).password(account.getPassword())
				.name(account.getName()).surname(account.getSurname())
				.birthday(DateFormatter.getDate(account.getBirthday())).country(Country.getValue(account.getCountry()))
				.city(account.getCity()).address(account.getAddress()).phoneNumber(account.getPhoneNumber())
				.gender(Gender.valueOf(account.getGender())).build();
	}

}
