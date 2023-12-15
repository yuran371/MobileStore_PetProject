package service;

import java.util.List;
import java.util.stream.Collectors;

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
		ItemsEntity itemsEntity = ItemsDao.getInstance().getByItemId(itemId).get();
		return new ItemsFindByIdDto(itemsEntity.getItemId(), itemsEntity.getModel(), itemsEntity.getBrand(),
				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency(),
				itemsEntity.getQuantity());
	}
}
