package mapper;


import dto.CreateAccountDto;
import entity.PersonalAccountEntity;
import org.junit.jupiter.api.Test;

public class CreateAccountMapperTest {

    @Test
    void mapDtoToEntity() {
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .email("email")
                .image(null)
                .name("name")
                .city("city")
                .address(null)
                .country("RUSSIA")
                .gender("MALE")
                .surname("surname")
                .phoneNumber("+79214098090")
                .password("password")
                .birthday("1999-02-10").build();

        PersonalAccountEntity accountDtoToPersonalAccountEntity =
                CreateAccountMapper.INSTANCE.createAccountDtoToPersonalAccountEntity(accountDto);
        System.out.println();
    }
}
