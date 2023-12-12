package service;

import java.util.List;
import java.util.stream.Collectors;

import dao.ItemsDao;
import dto.ItemsFilterDto;

public class ItemsService {

	static ItemsDao InstanceDao = ItemsDao.getInstance();

	public static List<ItemsFilterDto> itemsServiceMethod() {
		return InstanceDao.findAll().stream()
				.map(x -> new ItemsFilterDto(x.getModel(), x.getBrand(), x.getPrice(), x.getCurrency()))
				.collect(Collectors.toList());
	}
}
