package mapper;

import dto.personalAccount.ReadUserInfoDto;
import entity.PersonalAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION, uses = UserSellHistoryMapper.class)
public abstract class ReadUserInfoMapper {

    public static final ReadUserInfoMapper INSTANCE = Mappers.getMapper(ReadUserInfoMapper.class);

    @Mapping(target = "gender", source = "genderEnum")
    @Mapping(target = "country", source = "countryEnum")
    @Mapping(target = "purchases", source = "phonePurchases")
    @Mapping(target = "confirmed", source = "confirmedAccount")
    public abstract ReadUserInfoDto personalAccountEntityToUserInfoDto(PersonalAccountEntity entity);
        
}

