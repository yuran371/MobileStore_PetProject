package unit;

import entity.ProfileInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
        }

        @Nested
        @TestInstance(value = Lifecycle.PER_CLASS)
        @Tag(value = "PersonalAccountEntity")
        class PersonalAccount {

            @Test
            void add_newUserPaymentOptions_returnUserPaymentOptionFromDb() {
            }
        }
    }

    @Nested
    @TestInstance(value = Lifecycle.PER_CLASS)
    @Tag(value = "PersonalAccountEntity")
    class PremiumUser {

        @Test
        void premiumUserBuilder() {
//            Optional<Object> argument = PersonalAccountDaoTest.argumentsPersonalAccount()
//                    .flatMap(arguments -> Arrays.stream(arguments.get())).findFirst();
//            PersonalAccountEntity personalAccount = (PersonalAccountEntity) argument.get();
//            PremiumUserEntity premiumUserEntity = PremiumUserEntity.of(personalAccount, DiscountEnum.FIVE_PERCENT);
//            @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
//            @Cleanup Session session = sessionFactory.openSession();
//            session.beginTransaction();
//            session.persist(premiumUserEntity);
//            Long id = premiumUserEntity.getId();
//            session.clear();
//            PremiumUserEntity premiumUserEntityFromDb = session.get(PremiumUserEntity.class, id);
//            System.out.println();
//            session.getTransaction().commit();
        }
    }


    public static Stream<Arguments> getArgumentsForProfileInfo() {
        return Stream.of(Arguments.of(ProfileInfoEntity.builder().language("RU")
                                              .specialInfo("Want to have delivery between 12:00 and 14:00")
                                              .build()));
    }

}
