package service;

import java.util.List;
import java.util.stream.Collectors;

import dao.ItemsDao;
import dto.ItemsFindByIdDto;

public class ItemFindByIdService {
	private static ItemFindByIdService INSTANCE = new ItemFindByIdService();

	private ItemFindByIdService() {
	}

	public static ItemFindByIdService getInstance() {
		return INSTANCE;
	}

	static ItemsDao itemsDao = ItemsDao.getInstance();

	public List<ItemsFindByIdDto> itemsServiceMethod(Long itemId) {
		return itemsDao
				.getByItemId(itemId).stream()
				.map(dto -> new ItemsFindByIdDto(dto.getItemId(), dto.getModel(),
						dto.getBrand(), dto.getAttributes(), dto.getPrice(), dto.getCurrency(), dto.getQuantity()))
				.collect(Collectors.toList());
	}
}
