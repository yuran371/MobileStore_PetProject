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
                .internalMemory(itemsEntity.getInternalMemory().getInternalMemory())
                .ram(itemsEntity.getRam()
                        .getRam())
                .price(String.valueOf(itemsEntity.getItemSalesInformation()
                        .getPrice()))
                .currency(itemsEntity.getItemSalesInformation()
                        .getCurrency().toString())
                .quantity(itemsEntity.getItemSalesInformation().getQuantity().toString())
                .build();
        ItemsEntity actualToEntity = AddItemMapper.INSTANCE.toEntity(addItemDto);
        var actualToDto = AddItemMapper.INSTANCE.toDto(itemsEntity);
        assertThat(actualToEntity).isEqualTo(itemsEntity);
        assertThat(actualToDto).isEqualTo(addItemDto);
    }

}
