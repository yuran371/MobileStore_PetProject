package dto;

import dto.personalAccount.CreateAccountDto;
import entity.PersonalAccountEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mapper.CreateAccountMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utlis.Validators;
import validator.CreateUserGroup;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@Tag(value = "CreateAccountDto")
public class CreateAccountDtoTest {

    public static String basicNotBlankMessage = " must not be blank";
    public static String[] messages = {"email" + basicNotBlankMessage,
            "password" + basicNotBlankMessage,
            "name" + basicNotBlankMessage,
            "surname" + basicNotBlankMessage,
            "birthday" + basicNotBlankMessage,
            "country" + basicNotBlankMessage,
            "city" + basicNotBlankMessage,
            "address" + basicNotBlankMessage,
            "phone number" + basicNotBlankMessage,
            "gender" + basicNotBlankMessage};

    @Test
    void validUser_Validate() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email("email@email.ru")
                .image(null)
                .name("name")
                .city("city")
                .address("address")
                .country("RUSSIA")
                .gender("MALE")
                .surname("surname")
                .phoneNumber("+79214098090")
                .password("password12A")
                .birthday("1999-02-10").build();
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<CreateAccountDto>> validate = validator.validate(accountDto);
        assertThat(validate.size()).isEqualTo(0);
    }

    @Test
    void haveNullField_ThrowRightException() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email(null)
                .image(null)
                .name(null)
                .city(null)
                .address(null)
                .country(null)
                .gender(null)
                .surname(null)
                .phoneNumber(null)
                .password(null)
                .birthday(null)
                .build();
        ;
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<CreateAccountDto>> validate = validator.validate(accountDto);
        assertThat(validate.size()).isEqualTo(10);
        validate.stream()
                .forEach(violation -> assertThat(violation.getMessage()).containsAnyOf(messages));
    }

    @Test
    void notValidEmail_throwException() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email("email")
                .image(null)
                .name("name")
                .city("city")
                .address("address")
                .country("RUSSIA")
                .gender("MALE")
                .surname("surname")
                .phoneNumber("+79214098090")
                .password("password12A")
                .birthday("1999-02-10")
                .build();
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<CreateAccountDto>> validate = validator.validate(accountDto);
        assertThat(validate.size()).isEqualTo(1);
        assertThat(validate.stream().findFirst().get().getMessage()).isEqualTo("must be a well-formed email address");
    }

    @Test
    void notValidPassword_throwException() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email("email@email.ru")
                .image(null)
                .name("name")
                .city("city")
                .address("address")
                .country("RUSSIA")
                .gender("MALE")
                .surname("surname")
                .phoneNumber("+79214098090")
                .password("password")
                .birthday("1999-02-10")
                .build();
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<CreateAccountDto>> validate = validator.validate(accountDto);
        assertThat(validate.size()).isEqualTo(1);
        assertThat(validate.stream()
                           .findFirst().get().getMessage())
                .isEqualTo("password must have minimum one upper case letter and minimum one" +
                                   " number");
    }

    @Test
    void underEighteen_throwException() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email("email@email.ru")
                .image(null)
                .name("name")
                .city("city")
                .address("address")
                .country("RUSSIA")
                .gender("MALE")
                .surname("surname")
                .phoneNumber("+79214098090")
                .password("password12A")
                .birthday("2016-02-10").build();
        PersonalAccountEntity entity = CreateAccountMapper.INSTANCE.createAccountDtoToPersonalAccountEntity(accountDto);
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<PersonalAccountEntity>> validate = validator.validate(entity, CreateUserGroup.class);
        assertThat(validate.size()).isEqualTo(1);
        assertThat(validate.stream()
                           .findFirst().get().getMessage())
                .isEqualTo("your age should be over 18 years old");
    }
}
