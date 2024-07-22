package service;

public class OldItemFindByIdService {
	private static final OldItemFindByIdService INSTANCE = new OldItemFindByIdService();

	private OldItemFindByIdService() {
	}

	public static OldItemFindByIdService getInstance() {
		return INSTANCE;
	}

//	static ItemsRepository itemsDao = ItemsRepository.getInstance();

//	public ItemsFindByIdDto findById(Long itemId) {
//		ItemsEntity itemsEntity = ItemsDao.getInstance().getById(itemId).get();
//		return new ItemsFindByIdDto(itemsEntity.getId(), itemsEntity.getModel(), itemsEntity.getBrand().name(),
//				itemsEntity.getAttributes(), itemsEntity.getPrice(), itemsEntity.getCurrency().name(),
//				itemsEntity.getQuantity());
//	}
}
