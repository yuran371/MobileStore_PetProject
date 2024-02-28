package dao;

import entity.SellHistoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utlis.HibernateSessionFactory;

import java.util.Optional;
@Slf4j
public class SellHistoryDao implements Dao<Long, SellHistoryEntity> {

//	private static ItemsRepository itemsDao = ItemsRepository.getInstance();
	private static PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();

	private SellHistoryDao() {
	}

	private static class SingletonSellHistoryDaoCreator {
		private static final SellHistoryDao INSTANCE = new SellHistoryDao();
	}

	public static SellHistoryDao getInstance() {
		return SingletonSellHistoryDaoCreator.INSTANCE;
	}


	@Override
	public Optional<SellHistoryEntity> insert(SellHistoryEntity sellHistoryEntity) {
		try (SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
			 Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
			session.persist(sellHistoryEntity);
			transaction.commit();
			log.info("User {} successfully added", sellHistoryEntity);
			return Optional.ofNullable(sellHistoryEntity);
		}
	}

	@Override
	public Optional<SellHistoryEntity> getById(Long id) {
		return Optional.empty();
	}

	@Override
	public void delete(SellHistoryEntity sellHistoryEntity) {
	}

	@Override
	public void update(SellHistoryEntity entity) {

	}
}
