package dao;

import entity.ImportantStatisticEntity;
import jakarta.persistence.EntityManager;

public class ImportantStatisticDao extends DaoBase<Long, ImportantStatisticEntity> {
    public ImportantStatisticDao(EntityManager entityManager) {
        super(entityManager, ImportantStatisticEntity.class);
    }
}
