package dao;

import java.util.List;
import java.util.Optional;

import entity.SellHistoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utlis.HibernateSessionFactory;
@Slf4j
public class SellHistoryDao implements Dao<Long, SellHistoryEntity> {

	private static ItemsDao itemsDao = ItemsDao.getInstance();
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
	public Optional<Long> insert(SellHistoryEntity sellHistoryEntity) {
		try (SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
			 Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
			session.persist(sellHistoryEntity);
			transaction.commit();
			log.info("User {} successfully added", sellHistoryEntity);
			return Optional.ofNullable(sellHistoryEntity.getId());
		}
	}

	@Override
	public List<SellHistoryEntity> findAll() {
		return null;
	}

	@Override
	public Optional<SellHistoryEntity> getById(Long id) {
		return Optional.empty();
	}

	@Override
	public boolean delete(Long params) {
		return false;
	}
}
