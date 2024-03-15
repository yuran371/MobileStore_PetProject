package service;

import dao.ItemsDao;
import dto.AddItemDto;
import dto.ItemsInfoDto;
import dto.UpdateItemDto;
import dto.filter.AttributesFilter;
import entity.ItemsEntity;
import jakarta.inject.Inject;
import mapper.AddItemMapper;
import mapper.ItemsInfoMapper;
import mapper.UpdateItemMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemsService {

    @Inject
    public ItemsService(ItemsDao itemDao, SessionFactory sessionFactory) {
        this.itemDao = itemDao;
        this.sessionFactory = sessionFactory;
    }

    private final ItemsDao itemDao;
    private final SessionFactory sessionFactory;

    public Optional<ItemsEntity> insert(AddItemDto addItemDto) {
        AddItemMapper addItemMapper = AddItemMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        ItemsEntity entityFromDto = addItemMapper.toEntity(addItemDto);
        Optional<ItemsEntity> inserted = itemDao.insert(entityFromDto);
        session.getTransaction().commit();
        return inserted;
    }

    public void update(UpdateItemDto updateItemDto) {
        UpdateItemMapper updateItemMapper = UpdateItemMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        Optional<ItemsEntity> byId = itemDao.getById(updateItemDto.getId());
//        byId.ifPresentOrElse(v -> itemDao.update(updateItemMapper.toEntity(updateItemDto)), );
//        itemDao.getById(addItemDto.get)
//        itemDao.update(addItemMapper.toEntity(addItemDto));
        session.getTransaction().commit();
    }

    public ItemsInfoDto getById(long id) {
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        ItemsEntity itemsEntity = itemDao.getById(id)
                .get();
        session.getTransaction().commit();
        return itemsInfoMapper.toDto(itemsEntity);
    }

    public List<ItemsInfoDto> findItemsWithParameters(AttributesFilter filter, long page, long limit) {
        List<ItemsInfoDto> list = new ArrayList<>();
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        List<ItemsEntity> itemsWithParameters = itemDao.findItemsWithParameters(filter, page, limit);
        session.getTransaction().commit();
        for (ItemsEntity itemsWithParameter : itemsWithParameters) {
            list.add(itemsInfoMapper.toDto(itemsWithParameter));
        }
        return list;
    }

}