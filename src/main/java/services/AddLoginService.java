package services;

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
		if (account.address().isBlank() || account.city().isBlank()
				|| account.country().isBlank() && account.fullName().isBlank() || account.login().isBlank()
				|| account.phoneNumber().isBlank()) {
			return false;
		}
		return personalAccountDao.insertAccount(new PersonalAccountEntity(account.login(), account.fullName(),
				account.country(), account.city(), account.address(), account.phoneNumber()));
	}

}
