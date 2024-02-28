package dao;

import com.querydsl.jpa.impl.JPAQuery;
import entity.SellHistoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

import static entity.QSellHistoryEntity.sellHistoryEntity;

@Slf4j
public class SellHistoryDao extends DaoBase<Long, SellHistoryEntity> {
    public SellHistoryDao(EntityManager entityManager) {
        super(entityManager, SellHistoryEntity.class);
    }

    public List<SellHistoryEntity> getAllForUser(Long userId) {
        return new JPAQuery<SellHistoryEntity>(getEntityManager())
                .select(sellHistoryEntity)
                .from(sellHistoryEntity)
                .where(sellHistoryEntity.user.id.eq(userId))
                .fetch();
    }

    public List<SellHistoryEntity> getAllBeforeDate(OffsetDateTime date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SellHistoryEntity> criteria = cb
                .createQuery(SellHistoryEntity.class);
        Root<SellHistoryEntity> sellHistoryEntity = criteria.from(SellHistoryEntity.class);
         criteria.select(sellHistoryEntity)
                .where(cb.lessThanOrEqualTo(sellHistoryEntity.get("sellDate"), date));
        return getEntityManager().createQuery(criteria).getResultList();
    }
}
