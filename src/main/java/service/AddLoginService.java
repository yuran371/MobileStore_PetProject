package service;

import dao.PersonalAccountDao;
import dto.DtoPersonalAccount;
import entity.PersonalAccountEntity;

public class AddLoginService {

	private static AddLoginService INCTANCE = new AddLoginService();
	private static PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();

	private AddLoginService() {
	}

	public static AddLoginService getInctance() {
		return INCTANCE;
	}

	public boolean addAccount(DtoPersonalAccount account) {
		if (account.address().isBlank() || account.city().isBlank() || account.country().isBlank()
				|| account.name().isBlank() || account.email().isBlank() || account.phoneNumber().isBlank()
				|| account.surname().isBlank()) {
			return false;
		}
		return personalAccountDao.insertAccount(new PersonalAccountEntity(account.email(), account.name(),
				account.surname(), account.country().toLowerCase(), account.city().toLowerCase(), account.address(),
				account.phoneNumber()));
	}

}
