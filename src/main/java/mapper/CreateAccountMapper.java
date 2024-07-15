package mapper;

import dto.personalAccount.CreateAccountDto;
import entity.PersonalAccountEntity;
import jakarta.servlet.http.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public abstract class CreateAccountMapper {

    public static final CreateAccountMapper INSTANCE = Mappers.getMapper(CreateAccountMapper.class);
    private static final String USER_FOLDER = "user\\";
    private static final String DEFAULT_AVATAR = "user\\default-avatar-icon-of-social-media-user-vector.jpg";

    @Mapping(target = "countryEnum", source = "country")
    @Mapping(target = "genderEnum", source = "gender")
    @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "confirmedAccount", constant = "false")
    public abstract PersonalAccountEntity createAccountDtoToPersonalAccountEntity(CreateAccountDto dto);

    String mapImage(Part image) {
        return image == null ? DEFAULT_AVATAR : USER_FOLDER + image.getSubmittedFileName();
    }
}
