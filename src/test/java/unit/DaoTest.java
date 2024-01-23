package unit;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import entity.*;
import extentions.PersonalAccountParameterResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DaoTest {
    @Nested
    @Tag(value = "ItemsDao")
    class Items {
        ItemsDao itemsDao = ItemsDao.getInstance();
        ItemsEntity itemsEntity = ItemsEntity.builder().model("12").brand(BrandEnum.OnePlus)
                .attributes("512 gb white").price(99_999.99)
                .currency(CurrencyEnum.₽).quantity(233).build();

        @Test
        void insertMethodReturnsUserIdFromDB() {
            itemsDao.insert(itemsEntity);
            log.info("Just added: {} {} {} {} qt: {}", itemsEntity.getBrand(), itemsEntity.getModel(),
                    itemsEntity.getPrice(), itemsEntity.getCurrency(), itemsEntity.getQuantity());
        }

        @Test
        void findAllMethodReturnList() {
            List<ItemsEntity> all = itemsDao.findAll();
            assertThat(all).hasSize(15);
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
        @MethodSource("DaoTest#getArgumentForPersonalAccountTest")
        void insertMethodReturnsUserIdFromDB(PersonalAccountEntity account) {
            Long insert = instance.insert(account);
            assertThat(insert).isNotEqualTo(0);
        }

    }

    static Stream<Arguments> getArgumentForPersonalAccountTest() {
        return Stream.of(Arguments.of(PersonalAccountEntity.builder().address("no address")
                .birthday(LocalDate.now().minusYears(20)).city("no city").country(Country.KAZAKHSTAN)
                .email("noemail@email.ru").gender(Gender.MALE).image("").name("Sasha").password("123")
                .phoneNumber("+79214050505").surname("nonamich").build()));
    }
}
