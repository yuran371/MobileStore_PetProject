package service;

import java.util.List;
import java.util.stream.Collectors;

import dao.ItemsDao;
import dto.ItemsFilterDto;

public class ItemsService {
	private static ItemsService INSTANCE = new ItemsService();

	private ItemsService() {
	};

	static ItemsDao InstanceDao = ItemsDao.getInstance();

	public static ItemsService getInstance() {
		return INSTANCE;
	}

	public List<ItemsFilterDto> itemsServiceMethod() {
		return InstanceDao.findAll().stream()
				.map(dao -> new ItemsFilterDto(dao.getItemId(), dao.getModel(), dao.getBrand(), dao.getPrice(), dao.getCurrency()))
				.collect(Collectors.toList());
	}

}
