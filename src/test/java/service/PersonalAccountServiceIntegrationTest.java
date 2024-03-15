package service;

import dao.DaoTestFields;
import dto.personalAccount.AuthUserDto;
import dto.personalAccount.CreateAccountDto;
import dto.personalAccount.ReadUserInfoDto;
import dto.personalAccount.UpdateUserDto;
import extentions.AddTestEntitiesExtension;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@TestInstance(PER_METHOD)
@EnableWeld
@ExtendWith(AddTestEntitiesExtension.class)
public class PersonalAccountServiceIntegrationTest extends DaoTestFields {

    @WeldSetup
    WeldInitiator weldInitiator = WeldInitiator.of(WeldInitiator.createWeld().enableDiscovery());
    @Inject
    private PersonalAccountService testService;

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validCreateAccountDto")
    public void createUser_validUser_returnLeft(CreateAccountDto createAccountDto) {
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isNotNull();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validCreateAccountDto")
    public void createUser_notValidUser_returnRight(CreateAccountDto createAccountDto) {
        createAccountDto.setAddress(null);
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        assertThat(user.isRight()).isTrue();
        Set<? extends ConstraintViolation<?>> constraintViolations = user.get();
        assertThat(constraintViolations.size()).isEqualTo(1);
        Optional<? extends ConstraintViolation<?>> addressMustNotBeBlank = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getMessage().equals("address must not be blank"))
                .findFirst();
        assertThat(addressMustNotBeBlank).isNotEmpty();
    }


    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validCreateAccountDto")
    public void createUser_notValidEmail_returnRight(CreateAccountDto createAccountDto) {
        createAccountDto.setEmail("notemail");
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        Set<? extends ConstraintViolation<?>> constraintViolations = user.get();
        assertThat(constraintViolations.size()).isEqualTo(1);
        Optional<? extends ConstraintViolation<?>> addressMustNotBeBlank = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getMessage()
                        .equals("must be a well-formed email address"))
                .findFirst();
        assertThat(addressMustNotBeBlank).isNotEmpty();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validUpdateUserDto")
    public void update_validUser_returnLeft(UpdateUserDto updateUserDto) {
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isTrue();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validUpdateUserDto")
    public void update_notValidUser_returnRight(UpdateUserDto updateUserDto) {
        updateUserDto.setAddress(null);
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isRight()).isTrue();
        Set<? extends ConstraintViolation<?>> constraintViolations = user.get();
        assertThat(constraintViolations.size()).isEqualTo(1);
        Optional<? extends ConstraintViolation<?>> addressMustNotBeBlank = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getMessage().equals("address must not be blank"))
                .findFirst();
        assertThat(addressMustNotBeBlank).isNotEmpty();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#notValidUpdateUserDto")
    public void update_notValidId_returnLeftFalse(UpdateUserDto updateUserDto) {
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isFalse();
        System.out.println();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validUpdateUserDto")
    public void update_notValidEntity_returnRight(UpdateUserDto updateUserDto) {
        updateUserDto.setBirthday("2020-02-01");
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isRight()).isTrue();
        Set<? extends ConstraintViolation<?>> constraintViolations = user.get();
        assertThat(constraintViolations.size()).isEqualTo(1);
        Optional<? extends ConstraintViolation<?>> addressMustNotBeBlank = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getMessage()
                        .equals("your age should be over 18 years old"))
                .findFirst();
        assertThat(addressMustNotBeBlank).isNotEmpty();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validCreateAccountDtoToDelete")
    public void delete_deleted_returnTrue(CreateAccountDto createAccountDto) {
        Either<Long, Set<? extends ConstraintViolation<?>>> id = testService.createUser(createAccountDto);
        boolean deleted = testService.delete(id.getLeft());
        assertThat(deleted).isTrue();
    }


    @Tag("Unit")
    @Test
    public void delete_notDeleted_returnTrue() {
        Long id = 1000L;
        boolean deleted = testService.delete(id);
        assertThat(deleted).isFalse();
    }

    @Tag("Unit")
    @Test
    public void readUser_exist_returnDto() {
        Long id = 1L;
        Optional<ReadUserInfoDto> readUserInfoDto = testService.readUser(id);
        assertThat(readUserInfoDto).isNotEmpty();
        assertThat(readUserInfoDto.get().getId()).isEqualTo("1");
    }

    @Tag("Unit")
    @Test
    public void readUser_notExist_returnEmpty() {
        Long id = 1000L;
        Optional<ReadUserInfoDto> deleted = testService.readUser(id);
        assertThat(deleted).isEmpty();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validAuthUserDto")
    public void authUser_validDto_returnLeft(AuthUserDto authUserDto) {
        CreateAccountDto createAccountDto = validCreateAccountDtoToDelete().get(0);
        Either<Long, Set<? extends ConstraintViolation<?>>> user1 = testService.createUser(createAccountDto);
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> user = testService.authUser(authUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isNotEmpty();
        assertThat(testService.delete(user1.getLeft())).isTrue();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validAuthUserDto")
    public void authUser_notExistUser_returnLeftEmpty(AuthUserDto authUserDto) {
        authUserDto.setEmail("bebe@email.ru");
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> user = testService.authUser(authUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isEmpty();
    }
    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("service.PersonalAccountServiceIntegrationTest#validAuthUserDto")
    public void authUser_notValidEmail_returnRight(AuthUserDto authUserDto) {
        authUserDto.setEmail(null);
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> user = testService.authUser(authUserDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    public static List<CreateAccountDto> validCreateAccountDto() {
        return List.of(CreateAccountDto.builder().email("email@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10")
                               .build(), CreateAccountDto.builder().email("email2@email2.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").build());

    }

    public static List<CreateAccountDto> validCreateAccountDtoToDelete() {
        return List.of(CreateAccountDto.builder().email("email11@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10")
                               .build(), CreateAccountDto.builder().email("email22@email.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").build());

    }

    public static List<UpdateUserDto> validUpdateUserDto() {
        return List.of(UpdateUserDto.builder().email("update1@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10").id("1")
                               .build(),
                       UpdateUserDto.builder().email("update2@email.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").id("2")
                               .build());
    }

    public static List<UpdateUserDto> notValidUpdateUserDto() {
        return List.of(UpdateUserDto.builder().email("update3@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10").id("1000")
                               .build(),
                       UpdateUserDto.builder().email("update4@email.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").id("2000")
                               .build());
    }


    public static List<AuthUserDto> validAuthUserDto() {
        return List.of(AuthUserDto.builder().email("email11@email.ru").password("password1A").build());
    }

}
