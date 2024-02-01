package service;

import dao.ItemsDao;
import dto.ItemsDto;
import dto.ItemsFilterDto;
import entity.ItemsEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ItemsService {
	private static ItemsService INSTANCE = new ItemsService();

	private ItemsService() {
	};

	static ItemsDao InstanceDao = ItemsDao.getInstance();

	public static ItemsService getInstance() {
		return INSTANCE;
	}

	public List<ItemsFilterDto> findAllItems() {
		return InstanceDao.findAll().stream().map(entity -> new ItemsFilterDto(entity.getId(), entity.getModel(),
				entity.getBrand().name(), entity.getPrice(), entity.getCurrency().name())).collect(Collectors.toList());
	}

	public List<ItemsFilterDto> findBrand(String brand) {
		return InstanceDao.findByBrand(brand).stream().map(entity -> new ItemsFilterDto(entity.getId(),
				entity.getModel(), entity.getBrand().name(), entity.getPrice(), entity.getCurrency().name()))
				.collect(Collectors.toList());
	}

	public ItemsDto findById(Long itemId) {
		ItemsEntity itemsEntity = InstanceDao.getById(itemId).get();
		return new ItemsDto(itemsEntity.getId(), itemsEntity.getModel(), itemsEntity.getBrand().name(),
				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency().name(),
				itemsEntity.getQuantity());
	}

}
