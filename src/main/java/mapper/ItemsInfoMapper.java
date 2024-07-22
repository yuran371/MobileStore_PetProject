package mapper;

import dto.ItemsInfoDto;
import entity.ItemsEntity;
import entity.enums.Attributes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ItemsInfoMapper {
    public static final ItemsInfoMapper INSTANCE = Mappers.getMapper(ItemsInfoMapper.class);

    @Mapping(source = "price", target = "itemSalesInformation.price")
    @Mapping(source = "currency", target = "itemSalesInformation.currency")
    public abstract ItemsEntity toEntity(ItemsInfoDto itemsInfoDto);

    protected Attributes.BrandEnum stringToInputEnum(String brand) {
        for (Attributes.BrandEnum o : Attributes.BrandEnum.values()) {
            if (o.getBrand().equals(brand)) {
                return o;
            }
        }
        return null;
    }

    protected Attributes.OperatingSystemEnum stringToInputEnum1(String os) {
        for (Attributes.OperatingSystemEnum o : Attributes.OperatingSystemEnum.values()) {
            if (o.getOs().equals(os)) {
                return o;
            }
        }
        return null;
    }

    protected Attributes.InternalMemoryEnum stringToInputEnum2(String internalMemory) {
        for (Attributes.InternalMemoryEnum o : Attributes.InternalMemoryEnum.values()) {
            if (o.getInternalMemory().equals(internalMemory)) {
                return o;
            }
        }
        return null;
    }

    protected Attributes.RamEnum stringToInputEnum3(String ram) {
        for (Attributes.RamEnum o : Attributes.RamEnum.values()) {
            if (o.getRam().equals(ram)) {
                return o;
            }
        }
        return null;
    }

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
