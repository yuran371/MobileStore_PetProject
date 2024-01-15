package unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.PersonalAccountDao;
import dto.LoginUserDto;
import dto.ReadUserDto;
import entity.PersonalAccountEntity;
import extentions.PersonalAccountServiceExtension;
import service.LoginService;

public class ServiceTest {

	@Nested
	@Tag("PersonalAccountService")
	@TestInstance(Lifecycle.PER_METHOD)
	@ExtendWith({ PersonalAccountServiceExtension.class, MockitoExtension.class })
	class PersonalAccount {

		@Mock
		private LoginUserDto userDto;
		@Mock
		private PersonalAccountDao personalAccountDao;
		@InjectMocks
		private LoginService service;

		@Test
		@Tag("Unit")
		void serviceReturnExistingUserFromDB(PersonalAccountEntity account) {
			Mockito.doReturn(Optional.of(account)).when(personalAccountDao).getByPasswordAndLogin(any(), any());

			Optional<ReadUserDto> checkUser = service.checkUser(userDto);
			assertThat(checkUser).isNotEmpty();
		}

		@Test
		@Tag("Unit")
		void serviceReturnNullWhenUserNotExistInDB() {
			Mockito.doReturn(Optional.ofNullable(null)).when(personalAccountDao).getByPasswordAndLogin(any(), any());

			Optional<ReadUserDto> checkUser = service.checkUser(userDto);
			assertThat(checkUser).isEmpty();
		}

	}
}
