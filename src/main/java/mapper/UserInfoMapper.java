package mapper;

import dto.UserInfoDto;
import entity.PersonalAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION, uses = UserSellHistoryMapper.class)
public abstract class UserInfoMapper {

    public static UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(target = "gender", source = "genderEnum")
    @Mapping(target = "country", source = "countryEnum")
    @Mapping(target = "purchases", source = "phonePurchases")
    abstract UserInfoDto personalAccountEntityToUserInfoDto(PersonalAccountEntity entity);

}

