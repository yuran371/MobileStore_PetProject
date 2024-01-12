import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dao.PersonalAccountDao;
import entity.Country;
import entity.Gender;
import entity.PersonalAccountEntity;

public class DaoTest {

	@Nested
	@TestInstance(value = Lifecycle.PER_CLASS)
	@Tag(value = "PersonalAccount")
	class PersonalAccount {

		private final PersonalAccountDao DAO_INSTANCE = PersonalAccountDao.getInstance();
		PersonalAccountEntity account;

		@BeforeAll
		void createPersonalAccountEntity() {
			account = PersonalAccountEntity.builder().address("no address").birthday(LocalDate.now().minusYears(20))
					.city("no city").country(Country.KAZAKHSTAN).email("noemail@email.ru").gender(Gender.MALE).image("")
					.name("Sasha").password("123").phoneNumber("+79214050505").surname("nonamich").build();
		}

		@Test
		@Tag("Unit")
		void insertMethodReturnsUserIdFromDB() {
			Long insert = DAO_INSTANCE.insert(account);
			assertThat(insert).isNotEqualTo(0);
		}

	}

}
