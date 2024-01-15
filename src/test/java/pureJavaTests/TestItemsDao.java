package pureJavaTests;
import java.util.Arrays;

import dao.ItemsDao;
import entity.ItemsEntity;
import utlis.ConnectionPoolManager;

public class TestItemsDao {
	public static void main(String[] args) {
		ItemsDao itemsDao = ItemsDao.getInstance();
		try {
			int Apple = itemsDao.Insert(Arrays.asList(new ItemsEntity(null, "iPhone 14", "Apple", "128 gb black", 89_990.00, "₽", 83),
										new ItemsEntity(null, "iPhone 11", "Apple", "128 gb black", 61_599.65, "₽", 114),
										new ItemsEntity(null, "iPhone 15 Pro Max", "Apple", "512 gb grey", 200_599.99, "₽", 15),
										new ItemsEntity(null, "iPhone 14 Pro Max", "Apple", "256 gb grey", 70_599.99, "₽", 65)
										));
			int Xiaomi = itemsDao.Insert(Arrays.asList(new ItemsEntity(null, "Redmi A2+", "Xiaomi", "64 gb blue", 5_999.00, "₽", 1498),
					new ItemsEntity(null, "Redmi 12C", "Xiaomi", "128 gb grey", 9_499.65, "₽", 876),
					new ItemsEntity(null, "13T", "Xiaomi", "256 gb blue", 44_999.99, "₽", 47),
					new ItemsEntity(null, "12 Lite", "Xiaomi", "128 gb black", 25_499.99, "₽", 436)
					));
			int Samsung = itemsDao.Insert(Arrays.asList(new ItemsEntity(null, "Galaxy A04", "Samsung", "64 gb white", 12_999.99, "₽", 132),
					new ItemsEntity(null, "Galaxy S21 FE", "Samsung", "256 gb black", 28_499.99, "₽", 551),
					new ItemsEntity(null, "Galaxy S23 Ultra", "Samsung", "256 gb black", 114_999.99, "₽", 31),
					new ItemsEntity(null, "Galaxy S21 FE", "Samsung", "256 gb purple", 29_999.99, "₽", 977)
					));
		} finally {
			ConnectionPoolManager.closePool();
		}
	}
}
