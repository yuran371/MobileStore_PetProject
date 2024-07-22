package service;

import dao.ItemsDao;
import dto.AddItemDto;
import dto.ItemsInfoDto;
import dto.UpdateItemDto;
import dto.filter.AttributesFilter;
import entity.ItemsEntity;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mapper.AddItemMapper;
import mapper.ItemsInfoMapper;
import mapper.UpdateItemMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemsService {

    @Inject
    public ItemsService(ItemsDao itemDao, SessionFactory sessionFactory, Validator validator) {
        this.itemDao = itemDao;
        this.sessionFactory = sessionFactory;
        this.validator = validator;
    }

    private final ItemsDao itemDao;
    private final SessionFactory sessionFactory;
    private final Validator validator;

    public Either<Optional<ItemsEntity>, Set<? extends ConstraintViolation<?>>> insert(AddItemDto addItemDto) {
        Set<ConstraintViolation<Class<AddItemDto>>> dtoValidate = validator.validate(AddItemDto.class);
        if (!dtoValidate.isEmpty()) {
            return Either.right(dtoValidate);
        }
        ItemsEntity entityFromDto = AddItemMapper.INSTANCE.toEntity(addItemDto);
        Set<ConstraintViolation<Class<ItemsEntity>>> entityValidate = validator.validate(ItemsEntity.class);
        if (!entityValidate.isEmpty()) {
            return Either.right(entityValidate);
        }
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        Optional<ItemsEntity> inserted = itemDao.insert(entityFromDto);
        session.getTransaction()
                .commit();

        return Either.left(inserted);
    }

    public Either<Boolean, Set<? extends ConstraintViolation<?>>> update(UpdateItemDto updateItemDto) {
        Set<ConstraintViolation<Class<AddItemDto>>> dtoValidate = validator.validate(AddItemDto.class);
        if (!dtoValidate.isEmpty()) {
            return Either.right(dtoValidate);
        }
        ItemsEntity updateItemMapper = UpdateItemMapper.INSTANCE.toEntity(updateItemDto);
        Set<ConstraintViolation<Class<ItemsEntity>>> entityValidate = validator.validate(ItemsEntity.class);
        if (!entityValidate.isEmpty()) {
            return Either.right(entityValidate);
        }
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        Optional<ItemsEntity> byId = itemDao.getById(updateItemDto.getId());
        AtomicBoolean atomicRes = new AtomicBoolean(true);
        byId.ifPresentOrElse(v -> itemDao.update(updateItemMapper), () -> atomicRes.set(false));
        session.getTransaction()
                .commit();
        return Either.left(true);
    }

    public Optional<ItemsInfoDto> getById(long id) {
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        ItemsEntity itemsEntity = itemDao.getById(id)
                .get();
        Optional<ItemsInfoDto> getDto = Optional.of(itemsInfoMapper.toDto(itemsEntity));
        session.getTransaction()
                .commit();
        return getDto;
    }

    public List<ItemsInfoDto> findItemsWithParameters(AttributesFilter filter, long page, long limit) {
        List<ItemsInfoDto> list = new ArrayList<>();
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        List<ItemsEntity> itemsWithParameters = itemDao.findItemsWithParameters(filter, page, limit);
        session.getTransaction()
                .commit();
        for (ItemsEntity itemsWithParameter : itemsWithParameters) {
            list.add(itemsInfoMapper.toDto(itemsWithParameter));
        }
        return list;
    }

    public List<ItemsInfoDto> findBrand(String brand, long page, long limit) {
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        List<Optional<ItemsEntity>> items = itemDao.findBrand(brand, page, limit);
        List<ItemsInfoDto> collected = items.stream()
                .filter(Optional::isPresent)
                .map(optionalItemsEntity -> itemsInfoMapper.toDto(optionalItemsEntity.get()))
                .toList();
        session.getTransaction()
                .commit();
        return collected;
    }

    public List<ItemsInfoDto> findAllWithOffsetAndLimit(long page, long limit) {
        ItemsInfoMapper itemsInfoMapper = ItemsInfoMapper.INSTANCE;
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction()
                .begin();
        List<Optional<ItemsEntity>> items = itemDao.findAllWithOffsetAndLimit(page, limit);
        List<ItemsInfoDto> collected = items.stream()
                .filter(Optional::isPresent)
                .map(optionalItemsEntity -> itemsInfoMapper.toDto(optionalItemsEntity.get()))
                .toList();
        session.getTransaction()
                .commit();
        return collected;
    }

}