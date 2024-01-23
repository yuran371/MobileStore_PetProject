package service;

import dao.ItemsDao;
import dto.ItemsFindByIdDto;
import entity.ItemsEntity;

public class ItemFindByIdService {
	private static ItemFindByIdService INSTANCE = new ItemFindByIdService();

	private ItemFindByIdService() {
	}

	public static ItemFindByIdService getInstance() {
		return INSTANCE;
	}

	static ItemsDao itemsDao = ItemsDao.getInstance();

	public ItemsFindByIdDto findById(Long itemId) {
		ItemsEntity itemsEntity = ItemsDao.getInstance().getById(itemId).get();
		return new ItemsFindByIdDto(itemsEntity.getItemId(), itemsEntity.getModel(), itemsEntity.getBrand().name(),
				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency().name(),
				itemsEntity.getQuantity());
	}
}
