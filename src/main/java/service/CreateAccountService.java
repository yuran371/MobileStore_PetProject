package service;

import java.util.List;

import dao.PersonalAccountDao;
import dto.CreateAccountDto;
import exceptions.ValidationException;
import mapper.CreateAccountMapper;
import validator.CreateAccountValidator;
import validator.Error;

public class CreateAccountService {

	private static final CreateAccountService INSTANCE = new CreateAccountService();
	private final CreateAccountValidator validator = CreateAccountValidator.getInstance();
	private final CreateAccountMapper mapper = CreateAccountMapper.getInstance();
	private final PersonalAccountDao daoPersonalAccount = PersonalAccountDao.getInstance();

	private CreateAccountService() {
	}

	public static CreateAccountService getInstance() {
		return INSTANCE;
	}

	public Long save(CreateAccountDto account) {
		List<Error> createAccountErrors = validator.isValid(account).getCreateAccountErrors();
		if (!createAccountErrors.isEmpty()) {
			throw new ValidationException(createAccountErrors);
		}
		return daoPersonalAccount.insert(mapper.mapOf(account));
	}
}
