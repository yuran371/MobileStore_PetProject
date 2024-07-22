package mapper;

import dto.AddItemDto;
import entity.ItemsEntity;
import entity.enums.Attributes;
import entity.enums.Attributes.BrandEnum;
import entity.enums.Attributes.InternalMemoryEnum;
import entity.enums.Attributes.OperatingSystemEnum;
import entity.enums.Attributes.RamEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class AddItemMapper {

    public static final AddItemMapper INSTANCE = Mappers.getMapper(AddItemMapper.class);

    @Mapping(source = "price", target = "itemSalesInformation.price")
    @Mapping(source = "currency", target = "itemSalesInformation.currency")
    @Mapping(source = "quantity", target = "itemSalesInformation.quantity")
    public abstract ItemsEntity toEntity(AddItemDto addItemDto);

    protected BrandEnum stringToInputEnum(String brand) {
        for (BrandEnum o : BrandEnum.values()) {
            if (o.getBrand().equals(brand)) {
                return o;
            }
        }
        return null;
    }

    protected OperatingSystemEnum stringToInputEnum1(String os) {
        for (OperatingSystemEnum o : OperatingSystemEnum.values()) {
            if (o.getOs().equals(os)) {
                return o;
            }
        }
        return null;
    }

    protected InternalMemoryEnum stringToInputEnum2(String internalMemory) {
        for (InternalMemoryEnum o : InternalMemoryEnum.values()) {
            if (o.getInternalMemory().equals(internalMemory)) {
                return o;
            }
        }
        return null;
    }

    protected RamEnum stringToInputEnum3(String ram) {
        for (RamEnum o : RamEnum.values()) {
            if (o.getRam().equals(ram)) {
                return o;
            }
        }
        return null;
    }

    @Mapping(source = "itemSalesInformation.price", target = "price")
    @Mapping(source = "itemSalesInformation.currency", target = "currency")
    @Mapping(source = "itemSalesInformation.quantity", target = "quantity")
    public abstract AddItemDto toDto(ItemsEntity itemsEntity);

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
