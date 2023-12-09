import java.time.OffsetDateTime;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import entity.SellHistoryEntity;

public class TestSellHistoryDao {

	public static void main(String[] args) {
		System.out.println(ItemsDao.getByItemId(1L));
		SellHistoryDao.insert(new SellHistoryEntity(ItemsDao.getByItemId(1L).get(),
				PersonalAccountDao.getByLogin("chmod").get(), 1, OffsetDateTime.now()));
		System.out.println(ItemsDao.getByItemId(1L));
	}
}
