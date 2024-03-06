package mapper;

import dto.ItemsInfoDto;
import entity.ItemsEntity;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemsInfoMapperTest {

    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest#argumentsWithOneItem")
    void checkMapper_true(ItemsEntity itemsEntity) {
        ItemsInfoDto actual = ItemsInfoMapper.INSTANCE.toDto(itemsEntity);
        ItemsInfoDto expected = ItemsInfoDto.builder()
                .model(itemsEntity.getModel())
                .brand(itemsEntity.getBrand()
                        .getBrand())
                .color(itemsEntity.getColor())
                .os(itemsEntity.getOs()
                        .getOs())
                .image(itemsEntity.getImage())
                .internalMemory(itemsEntity.getInternalMemory()
                        .getCapacityInternal())
                .ram(itemsEntity.getRam()
                        .getCapacityRam())
                .price(itemsEntity.getItemSalesInformation()
                        .getPrice())
                .currency(itemsEntity.getItemSalesInformation()
                        .getCurrency()
                        .toString())
                .build();
        assertThat(actual).isEqualTo(expected);
    }

}
