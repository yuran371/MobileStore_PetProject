package mapper;

import dto.UpdateItemDto;
import entity.ItemsEntity;
import entity.enums.Attributes.BrandEnum;
import entity.enums.Attributes.InternalMemoryEnum;
import entity.enums.Attributes.OperatingSystemEnum;
import entity.enums.Attributes.RamEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class UpdateItemMapper {
    public static UpdateItemMapper INSTANCE = Mappers.getMapper(UpdateItemMapper.class);

    @Mapping(source = "id", target = "id")
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
    public abstract ItemsEntity toEntity(UpdateItemDto updateItemDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "os", target = "os")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "internalMemory", target = "internalMemory")
    @Mapping(source = "ram", target = "ram")
    @Mapping(source = "itemSalesInformation.price", target = "price")
    @Mapping(source = "itemSalesInformation.currency", target = "currency")
    @Mapping(source = "itemSalesInformation.quantity", target = "quantity")
    public abstract UpdateItemDto toDto(ItemsEntity itemsEntity);

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
            if (o.getInternalMemory()==internalMemory) {
                return o;
            }
        }
        return null;
    }

    protected RamEnum stringToInputEnum3(String ram) {
        for (RamEnum o : RamEnum.values()) {
            if (Objects.equals(o.getRam(), ram)) {
                return o;
            }
        }
        return null;
    }

    protected String inputEnumToString(BrandEnum brand) {
        return brand.getBrand();
    }

    protected String inputEnumToString(OperatingSystemEnum os) {
        return os.getOs();
    }

    protected String inputEnumToString(InternalMemoryEnum internalMemory) {
        return internalMemory.getInternalMemory();
    }

    protected String inputEnumToString(RamEnum ram) {
        return ram.getRam();
    }

}
