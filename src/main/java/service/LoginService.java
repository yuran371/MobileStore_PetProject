package service;

import java.util.Optional;

import dao.PersonalAccountDao;
import dto.LoginUserDto;
import dto.ReadUserDto;
import entity.PersonalAccountEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginService {

	private static final LoginService INSTANCE = new LoginService();
	private PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();

	public static LoginService getInstance() {
		return INSTANCE;
	}

	public Optional<ReadUserDto> checkUser(LoginUserDto user) {
		Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao
				.getByPasswordAndLogin(user.getEmail(), user.getPassword());
		if (personalAccountEntity.isPresent()) {
			return personalAccountEntity.map(entity -> ReadUserDto.builder().address(entity.getAddress())
					.birthday(entity.getBirthday()).city(entity.getCity()).country(entity.getCountry().name())
					.email(entity.getEmail()).image(entity.getImage()).name(entity.getName())
					.phoneNumber(entity.getPhoneNumber()).surname(entity.getSurname()).build());
		}
		return Optional.empty();
	}

}
