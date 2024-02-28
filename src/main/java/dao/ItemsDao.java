package dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import dto.AttributesFilter;
import entity.ItemsEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;

import static entity.QItemsEntity.QItemsEntity;

@Slf4j
public class ItemsDao extends DaoBase<Long, ItemsEntity> {

    public ItemsDao(EntityManager entityManager) {
        super( entityManager, ItemsEntity.class);
    }

    public List<ItemsEntity> findItemsOnSpecificPage(long page, long limit) {
        return new JPAQuery<ItemsEntity>(getEntityManager()).select(QItemsEntity)
                .from(QItemsEntity)
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
    }

    public List<ItemsEntity> findItemsWithParameters(AttributesFilter filter, Session session) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), QItemsEntity.brand::eq)
                .add(filter.getOs(), QItemsEntity.os::eq)
                .add(filter.getInternalMemory(), QItemsEntity.internalMemory::eq)
                .add(filter.getRam(), QItemsEntity.ram::eq)
                .buildAnd();
        return new JPAQuery<ItemsEntity>(session).select(QItemsEntity)
                .from(QItemsEntity)
                .where(predicate)
                .fetch();
    }

}
