package service;

import dao.PersonalAccountDao;
import dto.CreateAccountDto;
import io.vavr.control.Either;
import mapper.CreateAccountMapper;
import validator.CreateAccountValidator;
import validator.ValidationErrors;

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

	public Either<Long, ValidationErrors> save(CreateAccountDto account) {
		ValidationErrors createAccountErrors = validator.isValid(account);
		if (createAccountErrors.getCreateAccountErrors().isEmpty()) {
			return Either.left(daoPersonalAccount.insert(mapper.mapOf(account)));
		}
		return Either.right(createAccountErrors);
	}
}
