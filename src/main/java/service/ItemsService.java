package service;

import java.util.List;
import java.util.stream.Collectors;

import dao.ItemsDao;
import dto.ItemsDto;
import dto.ItemsFilterDto;
import entity.ItemsEntity;

public class ItemsService {
	private static ItemsService INSTANCE = new ItemsService();

	private ItemsService() {
	};

	static ItemsDao InstanceDao = ItemsDao.getInstance();

	public static ItemsService getInstance() {
		return INSTANCE;
	}

	public List<ItemsFilterDto> findAllItems() {
		return InstanceDao.findAll().stream().map(entity -> new ItemsFilterDto(entity.getItemId(), entity.getModel(),
				entity.getBrand(), entity.getPrice(), entity.getCurrency())).collect(Collectors.toList());
	}

	public List<ItemsFilterDto> findBrand(String brand) {
		return InstanceDao.findByBrand(brand).stream().map(entity -> new ItemsFilterDto(entity.getItemId(),
				entity.getModel(), entity.getBrand(), entity.getPrice(), entity.getCurrency()))
				.collect(Collectors.toList());
	}

//	public ItemsDto findById(Long itemId) {
//		ItemsEntity itemsEntity = InstanceDao.getByItemId(itemId).get();
//		return new ItemsDto(itemsEntity.getItemId(), itemsEntity.getModel(), itemsEntity.getBrand(),
//				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency(),
//				itemsEntity.getQuantity());
//	}

}
