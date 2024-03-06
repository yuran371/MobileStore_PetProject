package mapper;


import dto.AddItemDto;
import entity.ItemsEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class AddItemMapperTest {

    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest#argumentsWithOneItem")
    void checkk(ItemsEntity itemsEntity) {
        AddItemDto addItemDto = AddItemDto.builder()
                .model(itemsEntity.getModel())
                .brand(itemsEntity.getBrand()
                        .getBrand())
                .color(itemsEntity.getColor())
                .os(itemsEntity.getOs()
                        .getOs())
                .image(itemsEntity.getImage())
                .internalMemory(Integer.toString(itemsEntity.getInternalMemory().getCapacityInternal()))
                .ram(Integer.toString(itemsEntity.getRam()
                        .getCapacityRam()))
                .price(itemsEntity.getItemSalesInformation()
                        .getPrice())
                .currency(itemsEntity.getItemSalesInformation()
                        .getCurrency().toString())
                .quantity(itemsEntity.getItemSalesInformation().getQuantity().toString())
                .build();
        ItemsEntity actual = AddItemMapper.INSTANCE.toEntity(addItemDto);
        assertThat(actual).isEqualTo(itemsEntity);
    }

}
