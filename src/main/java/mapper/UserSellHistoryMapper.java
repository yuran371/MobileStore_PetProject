package mapper;

import dto.UserSellHistoryDto;
import entity.SellHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public abstract class UserSellHistoryMapper {

    public static UserSellHistoryMapper INSTANCE = Mappers.getMapper(UserSellHistoryMapper.class);

    @Mappings({
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "sellDate", source = "sellDate"),
            @Mapping(target = "brand", source = "itemId.brand"),
            @Mapping(target = "model", source = "itemId.model"),
            @Mapping(target = "os", source = "itemId.os"),
            @Mapping(target = "ram", source = "itemId.ram"),
            @Mapping(target = "color", source = "itemId.color"),
            @Mapping(target = "internalMemory", source = "itemId.internalMemory"),
            @Mapping(target = "price", source = "price")
    })
    abstract UserSellHistoryDto sellHistoryEntityToUserSellHistoryDto(SellHistoryEntity entity);

    String map(OffsetDateTime sellDate) {
        return sellDate.toString();
    }
}
