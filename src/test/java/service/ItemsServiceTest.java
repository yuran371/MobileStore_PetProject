package service;

import dao.ItemsDao;
import dto.AddItemDto;
import dto.ItemsInfoDto;
import dto.UpdateItemDto;
import dto.filter.AttributesFilter;
import entity.ItemsEntity;
import io.vavr.control.Either;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mapper.AddItemMapper;
import mapper.ItemsInfoMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utlis.Validators;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

//@ExtendWith(WeldJunit5Extension.class)
@TestInstance(PER_METHOD)
@Tag(value = "ItemsServiceTest")
public class ItemsServiceTest {

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @Mock
    ItemsDao itemsDao;
    @Spy
    Validator validator = Validators.defaultFactory.getValidator();
    @InjectMocks
    ItemsService itemsService;
    private AutoCloseable mockitoClosable;

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("argumentsOneAddItemsDto")
    void insertItem_validItem_returnItem(AddItemDto addItemDto) {
        mockitoClosable = MockitoAnnotations.openMocks(this);
        when(session.getTransaction())
                .thenReturn(transaction);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        doReturn(emptySet()).when(validator).validate(any(ItemsEntity.class), any());
        doReturn(Optional.of(AddItemMapper.INSTANCE.toEntity(addItemDto))).when(itemsDao).insert(any());
        Either<Optional<ItemsEntity>, Set<? extends ConstraintViolation<?>>> insertedItem = itemsService.insert(addItemDto);
        assertThat(insertedItem.isLeft()).isTrue();
        assertThat(insertedItem.getLeft()).isNotEmpty();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("argumentsOneUpdateItemsDto")
    void updateItem_validItem_returnItemWithId(UpdateItemDto updateItemDto) {
        mockitoClosable = MockitoAnnotations.openMocks(this);
        when(session.getTransaction())
                .thenReturn(transaction);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        doReturn(emptySet()).when(validator).validate(any(ItemsEntity.class), any());
        Either<Boolean, Set<? extends ConstraintViolation<?>>> updated = itemsService.update(updateItemDto);
        assertThat(updated.isLeft()).isTrue();
        assertThat(updated.getLeft()).isTrue();
//        itemsService.update(dto);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("argumentsOnItemsInfoDto")
    void getById_validItemDto_returnItemDto(ItemsInfoDto itemsInfoDto) {
        mockitoClosable = MockitoAnnotations.openMocks(this);
        when(session.getTransaction())
                .thenReturn(transaction);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        when(itemsDao.getById(anyLong())).thenReturn(Optional.of(ItemsInfoMapper.INSTANCE.toEntity(itemsInfoDto)));

        ItemsInfoDto actual = itemsService.getById(1l).get();
        assertThat(actual).isEqualTo(itemsInfoDto);
    }

    @Tag("Unit")
    @Test
    void findItemsWithParameters_validItemsWithFiltering_returnItemsList() {
        mockitoClosable = MockitoAnnotations.openMocks(this);
        when(session.getTransaction())
                .thenReturn(transaction);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        List<ItemsEntity> listMock = mock(List.class);
        AttributesFilter filterMock = mock(AttributesFilter.class);
        when(itemsDao.findItemsWithParameters(any(AttributesFilter.class), anyInt(), anyInt())).thenReturn(listMock);
        List<ItemsInfoDto> itemsWithParameters = itemsService.findItemsWithParameters(filterMock, 1, 3);
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