package service;

import dao.PersonalAccountDao;
import dto.CreateAccountDto;
import entity.PersonalAccountEntity;
import io.vavr.control.Either;
import lombok.SneakyThrows;
import mapper.CreateAccountMapper;
import validator.CreateAccountValidator;
import validator.ValidationErrors;

import java.util.Optional;

public class CreateAccountService {

	private static final CreateAccountService INSTANCE = new CreateAccountService();
	private final CreateAccountValidator validator = CreateAccountValidator.getInstance();
	private final CreateAccountMapper mapper = CreateAccountMapper.getInstance();
	private final PersonalAccountDao daoPersonalAccount = PersonalAccountDao.getInstance();
	private final ImageService imageService = ImageService.getInstance();

	private CreateAccountService() {
	}

	public static CreateAccountService getInstance() {
		return INSTANCE;
	}

	@SneakyThrows
	public Either<Optional<Long>, ValidationErrors> save(CreateAccountDto account) {
		ValidationErrors createAccountErrors = validator.isValid(account);
		if (createAccountErrors.getCreateAccountErrors().isEmpty()) {
			PersonalAccountEntity accountEntity = mapper.mapOf(account);
			imageService.upload(accountEntity.getImage(), account.getImage().getInputStream());
			return Either.left(daoPersonalAccount.insert(accountEntity));
		}
		return Either.right(createAccountErrors);
	}
}
