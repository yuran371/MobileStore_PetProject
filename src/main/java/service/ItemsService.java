package service;

import dto.ItemsFilterDto;

import java.util.List;

public class ItemsService {
	private static ItemsService INSTANCE = new ItemsService();

	private ItemsService() {
	};

//	static ItemsRepository InstanceDao = ItemsRepository.getInstance();

	public static ItemsService getInstance() {
		return INSTANCE;
	}

	public List<ItemsFilterDto> findAllItems() {
//		return InstanceDao.findAll().stream().map(entity -> new ItemsFilterDto(entity.getId(), entity.getModel(),
//				entity.getBrand().name(), entity.getPrice(), entity.getCurrency().name())).collect(Collectors.toList());
		return null;
	}

	public List<ItemsFilterDto> findBrand(String brand) {
//		return InstanceDao.findByBrand(brand).stream().map(entity -> new ItemsFilterDto(entity.getId(),
//				entity.getModel(), entity.getBrand().name(), entity.getPrice(), entity.getCurrency().name()))
//				.collect(Collectors.toList());
		return null;
	}

//	public ItemsDto findById(Long itemId) {
//		ItemsEntity itemsEntity = InstanceDao.getById(itemId).get();
//		return new ItemsDto(itemsEntity.getId(), itemsEntity.getModel(), itemsEntity.getBrand().name(),
//				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency().name(),
//				itemsEntity.getQuantity());
//	}

}
