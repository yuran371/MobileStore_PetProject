package mapper;

import dto.ItemsInfoDto;
import entity.ItemsEntity;
import entity.enums.Attributes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ItemsInfoMapper {
    public static ItemsInfoMapper INSTANCE = Mappers.getMapper(ItemsInfoMapper.class);

    @Mapping(source = "model", target = "model")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "os", target = "os")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "internalMemory", target = "internalMemory")
    @Mapping(source = "ram", target = "ram")
    @Mapping(source = "itemSalesInformation.price", target = "price")
    @Mapping(source = "itemSalesInformation.currency", target = "currency")
    public abstract ItemsInfoDto toDto(ItemsEntity itemsEntity);

    protected String inputEnumToString(Attributes.BrandEnum brand) {
        return brand.getBrand();
    }
    protected String inputEnumToString(Attributes.OperatingSystemEnum os) {
        return os.getOs();
    }
    protected String inputEnumToString(Attributes.InternalMemoryEnum internalMemory) {
        return internalMemory.getInternalMemory();
    }
    protected String inputEnumToString(Attributes.RamEnum ram) {
        return ram.getRam();
    }

}
