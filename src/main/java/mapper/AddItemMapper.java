package mapper;

import dto.AddItemDto;
import entity.ItemsEntity;
import entity.enums.Attributes.BrandEnum;
import entity.enums.Attributes.InternalMemoryEnum;
import entity.enums.Attributes.OperatingSystemEnum;
import entity.enums.Attributes.RamEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class AddItemMapper {
    public static AddItemMapper INSTANCE = Mappers.getMapper(AddItemMapper.class);

    @Mapping(source = "model", target = "model")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "os", target = "os")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "internalMemory", target = "internalMemory")
    @Mapping(source = "ram", target = "ram")
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
            if (o.getCapacityInternal()==Integer.parseInt(internalMemory)) {
                return o;
            }
        }
        return null;
    }

    protected RamEnum stringToInputEnum3(String ram) {
        for (RamEnum o : RamEnum.values()) {
            if (o.getCapacityRam()==Integer.parseInt(ram)) {
                return o;
            }
        }
        return null;
    }

}
