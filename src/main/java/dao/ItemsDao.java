package dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import dto.filter.AttributesFilter;
import entity.ItemsEntity;
import entity.enums.Attributes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static entity.QItemsEntity.itemsEntity;

@Slf4j
public class ItemsDao extends DaoBase<Long, ItemsEntity> {

    @Inject
    public ItemsDao(EntityManager entityManager) {
        super(entityManager, ItemsEntity.class);
    }

    public List<ItemsEntity> findItemsOnSpecificPage(long page, long limit) {
        return new JPAQuery<ItemsEntity>(getEntityManager()).select(itemsEntity)
                .from(itemsEntity)
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
    }

    public List<ItemsEntity> findItemsWithParameters(AttributesFilter filter, long page, long limit) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), itemsEntity.brand::eq)
                .add(filter.getOs(), itemsEntity.os::eq)
                .add(filter.getInternalMemory(), itemsEntity.internalMemory::eq)
                .add(filter.getRam(), itemsEntity.ram::eq)
                .buildAnd();
        return new JPAQuery<ItemsEntity>(getEntityManager()).select(itemsEntity)
                .from(itemsEntity)
                .where(predicate)
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
    }

    public List<Optional<ItemsEntity>> findBrand(String brand, long page, long limit) {
        List<ItemsEntity> items = new JPAQuery<ItemsEntity>(getEntityManager())
                .select(itemsEntity)
                .from(itemsEntity)
                .where(itemsEntity.brand.eq(Attributes.BrandEnum.valueOf(brand)))
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
        return items.stream().map(Optional::ofNullable).toList();
    }

    public List<Optional<ItemsEntity>> findAllWithOffsetAndLimit(long page, long limit) {
        List<ItemsEntity> items = new JPAQuery<ItemsEntity>(getEntityManager())
                .select(itemsEntity)
                .from(itemsEntity)
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
        return items.stream().map(Optional::ofNullable).toList();
    }

}

