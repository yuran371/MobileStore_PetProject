package service;

import dao.PersonalAccountDao;
import dto.personalAccount.AuthUserDto;
import dto.personalAccount.CreateAccountDto;
import dto.personalAccount.ReadUserInfoDto;
import dto.personalAccount.UpdateUserDto;
import entity.PersonalAccountEntity;
import io.vavr.collection.List;
import io.vavr.control.Either;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import mapper.UpdateUserMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utlis.Validators;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(PER_METHOD)
@Tag(value = "PersonalAccountServiceTest")
@RunWith(JUnitParamsRunner.class)
public class PersonalAccountServiceTest {

    @Mock
    PersonalAccountDao personalAccountDao;
    @Mock
    SessionFactory sessionFactory;
    @Spy
    Validator validator = Validators.defaultFactory.getValidator();
    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @InjectMocks
    PersonalAccountService testService;

    private AutoCloseable mockitoClosable;

    @Before
    public void setup() {
        mockitoClosable = MockitoAnnotations.openMocks(this);
    }

    @SneakyThrows
    @After
    public void finish() {
        mockitoClosable.close();
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validCreateAccountDto")
    public void createUser_validUser_returnLeft(CreateAccountDto createAccountDto) {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doReturn(emptySet()).when(validator).validate(any(PersonalAccountEntity.class), any());
        when(session.getTransaction()).thenReturn(transaction);
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isNull();
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validCreateAccountDto")
    public void createUser_notValidUser_returnRight(CreateAccountDto createAccountDto) {
        createAccountDto.setAddress(null);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        doReturn(emptySet()).when(validator).validate(any(PersonalAccountEntity.class), any());
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validCreateAccountDto")
    public void createUser_notValidEmail_returnRight(CreateAccountDto createAccountDto) {
        createAccountDto.setAddress(null);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        Set<ConstraintViolation<PersonalAccountEntity>> constraintViolations = new HashSet<>();

        constraintViolations.add(ConstraintViolationImpl.forBeanValidation(null, null, null, null, null, null, null,
                                                                           null, null, null, null));
        doReturn(constraintViolations).when(validator).validate(any(PersonalAccountEntity.class), any());
        Either<Long, Set<? extends ConstraintViolation<?>>> user = testService.createUser(createAccountDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validUpdateUserDto")
    public void update_validUser_returnLeft(UpdateUserDto updateUserDto) {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doReturn(emptySet()).when(validator).validate(any(PersonalAccountEntity.class), any());
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any())).thenReturn(Optional.of(UpdateUserMapper.INSTANCE.updateUserDtoToPersonalAccountEntity(updateUserDto)));
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isTrue();
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validUpdateUserDto")
    public void update_notValidUser_returnRight(UpdateUserDto updateUserDto) {
        updateUserDto.setAddress(null);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doReturn(emptySet()).when(validator).validate(any(PersonalAccountEntity.class), any());
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any()))
                .thenReturn(Optional.of(UpdateUserMapper.INSTANCE.updateUserDtoToPersonalAccountEntity(updateUserDto)));
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validUpdateUserDto")
    public void update_notValidEntity_returnRight(UpdateUserDto updateUserDto) {
        updateUserDto.setAddress(null);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        Set<ConstraintViolation<PersonalAccountEntity>> constraintViolations = new HashSet<>();

        constraintViolations.add(ConstraintViolationImpl.forBeanValidation(null, null, null, null, null, null, null,
                                                                           null, null, null, null));
        doReturn(constraintViolations).when(validator).validate(any(PersonalAccountEntity.class), any());
        Either<Boolean, Set<? extends ConstraintViolation<?>>> user = testService.update(updateUserDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    @Tag("Unit")
    @Test
    public void delete_deleted_returnTrue() {
        Long id = 1L;
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any())).thenReturn(Optional.empty());
        boolean deleted = testService.delete(id);
        assertThat(deleted).isTrue();
    }

    @Tag("Unit")
    @Test
    public void delete_notDeleted_returnTrue() {
        Long id = 1L;
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any())).thenReturn(Optional.of(PersonalAccountEntity.builder().build()));
        boolean deleted = testService.delete(id);
        assertThat(deleted).isFalse();
    }

    @Tag("Unit")
    @Test
    public void readUser_exist_returnDto() {
        Long id = 1L;
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any())).thenReturn(Optional.of(PersonalAccountEntity.builder().build()));
        Optional<ReadUserInfoDto> deleted = testService.readUser(id);
        assertThat(deleted).isNotEmpty();
    }

    @Tag("Unit")
    @Test
    public void readUser_notExist_returnEmpty() {
        Long id = 1L;
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        when(personalAccountDao.getById(any())).thenReturn(Optional.empty());
        Optional<ReadUserInfoDto> deleted = testService.readUser(id);
        assertThat(deleted).isEmpty();
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validAuthUserDto")
    public void authUser_notValidEmail_returnRight(AuthUserDto authUserDto) {
        authUserDto.setEmail(null);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> user = testService.authUser(authUserDto);
        assertThat(user.isRight()).isTrue();
        assertThat(user.get().size()).isEqualTo(1);
    }

    @Tag("Unit")
    @Test
    @Parameters(method = "validAuthUserDto")
    public void authUser_validDto_returnLeft(AuthUserDto authUserDto) {
        when(personalAccountDao.validateAuth(authUserDto.getEmail(), authUserDto.getPassword())).thenReturn(Optional.of(PersonalAccountEntity.builder().build()));
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> user = testService.authUser(authUserDto);
        assertThat(user.isLeft()).isTrue();
        assertThat(user.getLeft()).isNotEmpty();
    }


    public static Iterable<CreateAccountDto> validCreateAccountDto() {
        return List.of(CreateAccountDto.builder().email("email@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10")
                               .build(), CreateAccountDto.builder().email("email2@email2.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").build());

    }

    public static Iterable<UpdateUserDto> validUpdateUserDto() {
        return List.of(UpdateUserDto.builder().email("email@email.ru").image(null).name("name").city("city")
                               .address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password1A").birthday("1999-02-10").id("1")
                               .build(),
                       UpdateUserDto.builder().email("email2@email2.ru").image(null).name("name")
                               .city("city").address("address").country("RUSSIA").gender("MALE").surname("surname")
                               .phoneNumber("+79214098090").password("password2B").birthday("2000-03-11").id("2")
                               .build());
    }

    public static Iterable<AuthUserDto> validAuthUserDto() {
        return List.of(AuthUserDto.builder().email("email@email.ru").password("password1A").build());
    }
}
