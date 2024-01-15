import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.checkerframework.common.reflection.qual.GetClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dao.ItemsDao;
import entity.ItemsEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemsDaoTest {
	private ItemsEntity itemsEntity;
	private ItemsDao instanceDao = ItemsDao.getInstance();

	@BeforeEach
	void createEntity() {
		itemsEntity = ItemsEntity.builder().itemId(1l).model("Iphone").brand("Apple").attributes("spacegrey 128Gb")
				.price(1000.00).currency("$").quantity(100).build();
	}

	@Test
	@Tag("get")
	void getAllDao() {
		List<ItemsEntity> expected = instanceDao.findAll();
		List<ItemsEntity> actual = List.of(
				new ItemsEntity(1l, "iPhone 14", "Apple", "128 gb black", 89_990.00, "₽", 83),
				new ItemsEntity(2l, "iPhone 11", "Apple", "128 gb black", 61_599.65, "₽", 114),
				new ItemsEntity(3l, "iPhone 15 Pro Max", "Apple", "512 gb grey", 200_599.99, "₽", 15),
				new ItemsEntity(4l, "iPhone 14 Pro Max", "Apple", "256 gb grey", 70_599.99, "₽", 65),
				new ItemsEntity(5l, "Redmi A2+", "Xiaomi", "64 gb blue", 5_999.00, "₽", 1498),
				new ItemsEntity(6l, "Redmi 12C", "Xiaomi", "128 gb grey", 9_499.65, "₽", 876),
				new ItemsEntity(7l, "13T", "Xiaomi", "256 gb blue", 44_999.99, "₽", 47),
				new ItemsEntity(8l, "12 Lite", "Xiaomi", "128 gb black", 25_499.99, "₽", 436),
				new ItemsEntity(9l, "Galaxy A04", "Samsung", "64 gb white", 12_999.99, "₽", 132),
				new ItemsEntity(10l, "Galaxy S21 FE", "Samsung", "256 gb black", 28_499.99, "₽", 551),
				new ItemsEntity(11l, "Galaxy S23 Ultra", "Samsung", "256 gb black", 114_999.99, "₽", 31),
				new ItemsEntity(12l, "Galaxy S21 FE", "Samsung", "256 gb purple", 29_999.99, "₽", 977));
		assertEquals(expected, actual);
		assertThat(actual).hasSameSizeAs(expected);		//check assetj.core library
	}

	@Test
	@Tag("delete")
	void deleteByIdDao() {
		boolean delete = instanceDao.delete(99l);
		assertFalse(delete);
	}

	@Test
	@Tag("get")
	void testingGetByIdDao() {
		Optional<ItemsEntity> expected = instanceDao.getById(1l);
		Optional<ItemsEntity> actual = Optional
				.of(new ItemsEntity(1l, "iPhone 14", "Apple", "128 gb black", 89_990.00, "₽", 83));
		assertEquals(expected, actual);
	}
}
