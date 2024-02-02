package extentions;

import java.time.LocalDate;

import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import entity.PersonalAccountEntity;

public class LoginServiceExtension implements BeforeAllCallback, ParameterResolver {

	private String personalAccountEntity = "Entity";
	private Namespace storeName = Namespace.create(personalAccountEntity);

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Store store = context.getStore(storeName);
		store.put(personalAccountEntity,
				PersonalAccountEntity.builder().address("no address").birthday(LocalDate.now().minusYears(20))
						.city("no city").country(CountryEnum.KAZAKHSTAN).email("noemail@email.ru").genderEnum(GenderEnum.MALE)
						.image("").name("Sasha").password("123").phoneNumber("+79214050505").surname("nonamich")
						.build());

	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == PersonalAccountEntity.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return extensionContext.getStore(storeName).get(personalAccountEntity);
	}
}
