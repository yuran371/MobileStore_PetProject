package service;

public class PersonalAccountService {

	private final static PersonalAccountService INCTANCE = new PersonalAccountService();
//	private static PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();

	private PersonalAccountService() {
	}

	public static PersonalAccountService getInctance() {
		return INCTANCE;
	}

	/*
	 * public Optional<Long> addAccount(CreateAccountDto account) { // if
	 * (account.address().isBlank() || account.city().isBlank() ||
	 * account.country().isBlank() // || account.name().isBlank() ||
	 * account.email().isBlank() || account.phoneNumber().isBlank() // ||
	 * account.surname().isBlank()) { // return Optional.empty(); // }
	 * 
	 * // return personalAccountDao.insertAccount(new
	 * PersonalAccountEntity(account.email(), account.name(), // account.surname(),
	 * account.country().toLowerCase(), account.city().toLowerCase(),
	 * account.address(), // account.phoneNumber()));
	 * 
	 * boolean accountBlank = Stream.of(account.address(), account.city(),
	 * account.country(), account.name(), account.email(), account.phoneNumber(),
	 * account.surname()) .anyMatch(String::isBlank);
	 * 
	 * return accountBlank ? Optional.empty() : personalAccountDao.insertAccount(new
	 * PersonalAccountEntity(account.email(), account.name(), account.surname(),
	 * account.country().toLowerCase(), account.city().toLowerCase(),
	 * account.address(), account.phoneNumber()));
	 * 
	 * }
	 * 
	 * public CreateAccountDto getById(Long id) { PersonalAccountEntity byIdEntity =
	 * personalAccountDao.getByID(id).get(); return new
	 * CreateAccountDto(byIdEntity.getAccountId(), byIdEntity.getEmail(),
	 * byIdEntity.getName(), byIdEntity.getSurname(), byIdEntity.getCountry(),
	 * byIdEntity.getCity(), byIdEntity.getAddress(), byIdEntity.getPhoneNumber());
	 * }
	 */
}
