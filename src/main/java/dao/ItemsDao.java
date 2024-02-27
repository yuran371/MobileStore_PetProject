package dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import dto.AttributesFilter;
import entity.ItemsEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static entity.QItemsEntity.itemsEntity;

@Slf4j
public class ItemsDao extends DaoBase<Long, ItemsEntity> {

    public ItemsDao(EntityManager entityManager) {
        super(entityManager, ItemsEntity.class);
    }

    public Optional<Long> insertViaHibernate(ItemsEntity items, Session session) {
        session.persist(items);
        return Optional.ofNullable(items.getId());
    }

    public List<ItemsEntity> findByBrandViaHQL(String brand, Session session) {
        return session.createQuery("select i from items i " + "where i.brand = :brand", ItemsEntity.class)
                .setParameter("brand", brand)
                .list();
    }

    public List<ItemsEntity> findItemsOnSpecificPage(long page, Session session) {
        long limit = 3;
        return new JPAQuery<ItemsEntity>(session).select(itemsEntity)
                .from(itemsEntity)
                .limit(limit)
                .offset(Math.abs(limit * page - limit))
                .fetch();
    }

    public List<ItemsEntity> findAllViaQuerydsl() {
        return new JPAQuery<ItemsEntity>(getEntityManager()).select(itemsEntity)
                .from(itemsEntity)
                .fetch();
    }

    public List<ItemsEntity> findItemsWithParameters(AttributesFilter filter, Session session) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), itemsEntity.brand::eq)
                .add(filter.getOs(), itemsEntity.os::eq)
                .add(filter.getInternalMemory(), itemsEntity.internalMemory::eq)
                .add(filter.getRam(), itemsEntity.ram::eq)
                .buildAnd();
        return new JPAQuery<ItemsEntity>(session).select(itemsEntity)
                .from(itemsEntity)
                .where(predicate)
                .fetch();
    }

}
