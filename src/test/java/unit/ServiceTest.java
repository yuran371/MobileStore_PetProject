package unit;

import dao.PersonalAccountDao;
import dto.LoginUserDto;
import entity.PersonalAccountEntity;
import extentions.LoginServiceExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.LoginService;

public class ServiceTest {

	@Nested
	@Tag("PersonalAccountService")
	@TestInstance(Lifecycle.PER_METHOD)
	@ExtendWith({ LoginServiceExtension.class, MockitoExtension.class })
	class Login {

		@Mock
		private LoginUserDto userDto;
		@Mock
		private PersonalAccountDao personalAccountDao;
		@InjectMocks
		private LoginService service;

		@Test
		@Tag("Unit")
		void serviceReturnExistingUserFromDB(PersonalAccountEntity account) {
//			Mockito.doReturn(Optional.of(account)).when(personalAccountDao).validateAuth(any(), any());
//
//			Optional<ReadUserDto> checkUser = service.checkUser(userDto);
//			assertThat(checkUser).isNotEmpty();
		}

		@Test
		@Tag("Unit")
		void serviceReturnNullWhenUserNotExistInDB() {
//			Mockito.doReturn(Optional.ofNullable(null)).when(personalAccountDao).validateAuth(any(), any());
//
//			Optional<ReadUserDto> checkUser = service.checkUser(userDto);
//			assertThat(checkUser).isEmpty();
		}

	}
}
