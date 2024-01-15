package integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;

import dao.PersonalAccountDao;
import dto.LoginUserDto;
import dto.ReadUserDto;
import entity.PersonalAccountEntity;
import extentions.LoginServiceExtension;
import service.LoginService;

@Tag("LoginService")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ LoginServiceExtension.class })
public class LoginServiceIntegrationTest extends IntegrationTestBase {

	@Spy
	private PersonalAccountDao personalAccountDao;
	@Spy
	private LoginService service;
	private String userId = "";

	@Test
	void serviceReturnExistingUserFromDb(PersonalAccountEntity entity) {
		personalAccountDao.insert(entity);
		LoginUserDto user = LoginUserDto.builder().email(entity.getEmail()).password(entity.getPassword()).build();
		Optional<ReadUserDto> checkUser = service.checkUser(user);
		userId = checkUser.isPresent() ? checkUser.get().getEmail() : "";
		assertAll(() -> assertThat(checkUser).isNotEmpty(),
				() -> assertThat(checkUser.get().getEmail()).isEqualTo(entity.getEmail()));
	}

	@AfterAll
	void deleteUser() {
		Optional<PersonalAccountEntity> byLogin = personalAccountDao.getByLogin(userId);
		if (!userId.isBlank() && byLogin.isPresent()) {
			personalAccountDao.delete(byLogin.get().getAccountId());
		}
	}
}
