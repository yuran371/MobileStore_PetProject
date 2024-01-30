package unit;

import entity.PersonalAccountEntity;
import entity.ProfileInfoEntity;
import entity.UserPaymentOptions;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.HibernateTestUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@Slf4j
public class EntityTest {

    @Nested
    @TestInstance(value = Lifecycle.PER_CLASS)
    @Tag(value = "ProfileInfoEntity")
    class ProfileInfo {

        @ParameterizedTest
        @MethodSource("unit.EntityTest#getArgumentsForProfileInfo")
        void setPersonalAccount_setNewAccount_AddAccountInDb(ProfileInfoEntity infoEntity) {
            Optional<Object> argument = DaoTest.getArgumentForPersonalAccountTest()
                    .flatMap(arguments -> Arrays.stream(arguments.get())).findFirst();
            PersonalAccountEntity personalAccountEntity = (PersonalAccountEntity) argument.get();
            @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(personalAccountEntity);
            session.persist(infoEntity);
            infoEntity.setPersonalAccount(personalAccountEntity);
            session.flush();
            session.refresh(infoEntity);
            session.getTransaction().commit();
            Assertions.assertAll(
                    () -> assertThat(infoEntity.getPersonalAccount()).isNotNull(),
                    () -> assertThat(personalAccountEntity.getProfileInfo()).isNotNull()
            );
            session.beginTransaction();
            session.remove(personalAccountEntity);
            session.remove(infoEntity);
            session.getTransaction().commit();
        }
    }

    @Nested
    @TestInstance(value = Lifecycle.PER_CLASS)
    @Tag(value = "PersonalAccountEntity")
    class PersonalAccount {

        @Test
        void add_newUserPaymentOptions_returnUserPaymentOptionFromDb() {
            Optional<Object> argument = DaoTest.getArgumentForPersonalAccountTest()
                    .flatMap(arguments -> Arrays.stream(arguments.get())).findFirst();
            PersonalAccountEntity personalAccountEntity = (PersonalAccountEntity) argument.get();
            @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(personalAccountEntity);
            Long accountId = personalAccountEntity.getAccountId();
            personalAccountEntity.getPaymentOptions().add(UserPaymentOptions.of("credit card"));
            personalAccountEntity.getPaymentOptions().add(UserPaymentOptions.of("cash"));
            session.flush();
            session.getTransaction().commit();
            session.beginTransaction();
            PersonalAccountEntity accountFromDb = session.get(PersonalAccountEntity.class, accountId);
            assertThat(accountFromDb.getPaymentOptions().size()).isEqualTo(2);
            session.remove(accountFromDb);
            session.getTransaction().commit();
        }
    }


    public static Stream<Arguments> getArgumentsForProfileInfo() {
        return Stream.of(Arguments.of(ProfileInfoEntity.builder().language("RU")
                                              .specialInfo("Want to have delivery between 12:00 and 14:00").build()));
    }

}
