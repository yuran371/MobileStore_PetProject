package pureJavaTests;
import java.time.OffsetDateTime;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import entity.SellHistoryEntity;

public class TestSellHistoryDao {

	public static void main(String[] args) {
		ItemsDao itemsDao = ItemsDao.getInstance();
		PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();
		System.out.println(itemsDao.getById(1L));
		SellHistoryDao.insert(new SellHistoryEntity(itemsDao.getById(1L).get(),
				personalAccountDao.getByLogin("chmod").get(), 1, OffsetDateTime.now()));
		System.out.println(itemsDao.getById(1L));
	}
}
