package service;

import dto.AddItemDto;
import dto.ItemsInfoDto;
import dto.UpdateItemDto;
import dto.filter.AttributesFilter;
import entity.ItemsEntity;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import mapper.AddItemMapper;
import mapper.ItemsInfoMapper;
import mapper.UpdateItemMapper;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@EnableWeld     // or @ExtendWith(WeldJUnit5Extension.class)
@TestInstance(PER_METHOD)
@Tag(value = "ItemsServiceTest")
public class ItemsServiceIntegrationTest {

    @Inject
    private ItemsService itemsService;
    @WeldSetup
    WeldInitiator weldInitiator = WeldInitiator.of(WeldInitiator.createWeld()
            .enableDiscovery());

    @Tag("Integration")
    @ParameterizedTest
    @MethodSource("argumentsOneAddItemsDto")
    void insertItem_validItem_returnItem(AddItemDto addItemDto) {
        Either<Optional<ItemsEntity>, Set<? extends ConstraintViolation<?>>> insertedItem =
                itemsService.insert(addItemDto);
        System.out.println();
        assertThat(insertedItem.isLeft()).isTrue();
        assertThat(insertedItem.getLeft()).isNotEmpty();
    }

    @Tag("Integration")
    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest#argumentsWithOneItem")
    void updateItem_validItem_returnItemWithId(ItemsEntity itemsEntity) {
//        itemsService.insert(updateItemDto);
        AddItemDto addItemDto = AddItemMapper.INSTANCE.toDto(itemsEntity);
        itemsService.insert(addItemDto);
        UpdateItemDto updateItemDto = UpdateItemMapper.INSTANCE.toDto(itemsEntity);
        updateItemDto.setColor("Izmenen");
        updateItemDto.setId(3l);
        Either<Boolean, Set<? extends ConstraintViolation<?>>> updated = itemsService.update(updateItemDto);
        assertThat(updated.isLeft()).isTrue();
        assertThat(updated.getLeft()).isTrue();

    }

    @Tag("Integration")
    @ParameterizedTest
    @MethodSource("service.ItemsServiceTest#argumentsOneAddItemsDto")
    void getItemById_validItem_returnItemDto(AddItemDto addItemDto) {
        Either<Optional<ItemsEntity>, Set<? extends ConstraintViolation<?>>> inserted = itemsService.insert(addItemDto);
        Optional<ItemsInfoDto> ItemById = itemsService.getById(1l);
        assertThat(ItemById).isEqualTo(addItemDto);
        System.out.println();
    }

    @Tag("Integration")
    @ParameterizedTest
    @MethodSource("service.ItemsServiceTest#argumentsOnItemsInfoDto")
    void findItemsWithParameters_validItemsWithFiltering_returnItemsList(ItemsInfoDto itemsInfoDto) {   //как я понимаю должно передаваться значение буля th+checkbox
        boolean isBrand = true;
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        ItemsEntity entity = itemsInfoMapper.toEntity(itemsInfoDto);
        AttributesFilter.builder().brand(entity.getBrand()).build();

    }

    public static Stream<Arguments> argumentsOneAddItemsDto() {
        return Stream.of(Arguments.of(AddItemDto.builder()
                .brand("Google")
                .model("pixel a5")
                .internalMemory("16")
                .ram("4")
                .color("yellow")
                .os("Android")
                .image("/ava-path")
                .price("999.99")
                .currency("$")
                .quantity("57")
                .build()));
    }

    public static Stream<Arguments> argumentsOneUpdateItemsDto() {
        return Stream.of(Arguments.of(UpdateItemDto.builder()
                .brand("Google")
                .model("pixel a5")
                .internalMemory("16")
                .ram("4")
                .color("yellow")
                .os("Android")
                .image("/ava-path")
                .price("999.99")
                .currency("$")
                .quantity("57")
                .build()));
    }

    public static Stream<Arguments> argumentsOnItemsInfoDto() {
        return Stream.of(Arguments.of(ItemsInfoDto.builder()
                .brand("Google")
                .model("pixel a5")
                .internalMemory("16")
                .ram("4")
                .color("yellow")
                .os("Android")
                .image("/ava-path")
                .price("999.99")
                .currency("$")
                .build()));
    }

}