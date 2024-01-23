package unit;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import entity.*;
import extentions.PersonalAccountParameterResolver;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utlis.HibernateSessionFactory;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoTest {
    @Nested
    @Tag(value = "ItemsDao")
    class Items {
        ItemsDao itemsDao = ItemsDao.getInstance();
        ItemsEntity itemsEntity = ItemsEntity.builder().model("xperia").brand("sony").attributes("32gb").price(999.99)
                .currency(CurrencyEnum.₸.name()).quantity(13).build();

        @Test
        void insertMethodReturnsUserIdFromDB() {
            itemsDao.insert(itemsEntity);
        }
    }

    @Nested
    @TestInstance(value = Lifecycle.PER_CLASS)
    @Tag(value = "PersonalAccountDao")
    @ExtendWith(value = {PersonalAccountParameterResolver.class})
    class PersonalAccount {

        private PersonalAccountDao instance;

        public PersonalAccount(PersonalAccountDao instance) {
            this.instance = instance;
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("getArgumentForPersonalAccountTest")
        void insert_NewUser_notNull(PersonalAccountEntity account) {
            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            sessionFactory.close();

            Long insert = instance.insert(account);
            assertThat(insert).isNotNull();
        }
        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("getArgumentForPersonalAccountTest")
        void insertMethodAddUserReturnsUserIdFromDB(PersonalAccountEntity account) {
            Long insert = instance.insert(account);
            assertThat(insert).isNotNull();
        }

        static Stream<Arguments> getArgumentForPersonalAccountTest() {
            return Stream.of(Arguments.of(PersonalAccountEntity.builder().address("no address")
                                                  .birthday(LocalDate.now().minusYears(20)).city("no city")
                                                  .country(Country.KAZAKHSTAN)
                                                  .email("noemail@email.ru").gender(Gender.MALE).image("").name("Sasha")
                                                  .password("123")
                                                  .phoneNumber("+79214050505").surname("nonamich").build()));
        }

    }
}
