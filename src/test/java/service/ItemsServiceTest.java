package service;

import dao.ItemsDao;
import dto.AddItemDto;
import dto.ItemsInfoDto;
import entity.ItemSalesInformationEntity;
import entity.ItemsEntity;
import entity.enums.Attributes;
import entity.enums.CurrencyEnum;
import mapper.AddItemMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static entity.enums.Attributes.BrandEnum.GOOGLE;
import static entity.enums.Attributes.OperatingSystemEnum.ANDROID;
import static entity.enums.Attributes.RamEnum.gb_4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

//@ExtendWith(WeldJunit5Extension.class)
public class ItemsServiceTest {

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @Mock
    ItemsDao itemsDao;
    @InjectMocks
    ItemsService itemsService;

    @Test
    void insertService_dto_UNIT() {
        ItemsEntity itemsEntity = ItemsEntity.builder()
                .brand(GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(gb_4)
                .color("yellow")
                .os(ANDROID)
                .image(new byte[0])
                .itemSalesInformation(ItemSalesInformationEntity.builder()
                        .price(999.99)
                        .currency(CurrencyEnum.$)
                        .quantity(57)
                        .build())
                .build();

        AddItemMapper addItemMapper = AddItemMapper.INSTANCE;
        AddItemDto dto = addItemMapper.toDto(itemsEntity);
        var openMocks = MockitoAnnotations.openMocks(this);
        Mockito.when(session.getTransaction())
                .thenReturn(transaction);
        Mockito.when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        Mockito.doReturn(Optional.of(itemsEntity))
                .when(itemsDao)
                .insert(itemsEntity);
        Optional<ItemsEntity> inserted = itemsService.insert(dto);
        assertThat(inserted.get()).isEqualTo(itemsEntity);

    }

    @Test
    void getIdService_dto_UNIT() {
        var openMocks = MockitoAnnotations.openMocks(this);
        AddItemDto addItemDto = AddItemDto.builder()
                .model("PixelTest")
                .brand("Google")
                .color("Cherniy")
                .os("Android")
                .image(new byte[0])
                .internalMemory("512")
                .ram("16")
                .price("499.99")
                .currency("$")
                .quantity("69")
                .build();
        ItemsEntity itemsEntity = ItemsEntity.builder()
                .brand(GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(gb_4)
                .color("yellow")
                .os(ANDROID)
                .image(new byte[0])
                .itemSalesInformation(ItemSalesInformationEntity.builder()
                        .price(999.99)
                        .currency(CurrencyEnum.$)
                        .quantity(57)
                        .build())
                .build();
        Mockito.when(session.getTransaction())
                .thenReturn(transaction);
        Mockito.when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        Mockito.doReturn(Optional.of(itemsEntity))
                .when(itemsDao)
                .getById(anyLong());
        ItemsInfoDto byId = itemsService.getById(4);
        System.out.println(byId);
    }

    @Test
    void updateService_dto_UNIT() {
        var openMocks = MockitoAnnotations.openMocks(this);
        ItemsEntity itemsEntity = ItemsEntity.builder()
                .brand(GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(gb_4)
                .color("yellow")
                .os(ANDROID)
                .image(new byte[0])
                .itemSalesInformation(ItemSalesInformationEntity.builder()
                        .price(999.99)
                        .currency(CurrencyEnum.$)
                        .quantity(57)
                        .build())
                .build();
        AddItemMapper addItemMapper = AddItemMapper.INSTANCE;
        AddItemDto dto = addItemMapper.toDto(itemsEntity);
        Mockito.when(session.getTransaction())
                .thenReturn(transaction);
        Mockito.when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        Mockito.doReturn(Mockito.doNothing())
                .when(itemsDao)
                .update(itemsEntity);
//        itemsService.update(dto);
    }

}