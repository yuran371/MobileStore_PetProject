package extentions;

import entity.PersonalAccountEntity;
import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.time.LocalDate;

public class LoginServiceExtension implements BeforeAllCallback, ParameterResolver {

	private final String personalAccountEntity = "Entity";
	private Namespace storeName = Namespace.create(personalAccountEntity);

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Store store = context.getStore(storeName);
		store.put(personalAccountEntity,
				PersonalAccountEntity.builder().address("no address").birthday(LocalDate.now().minusYears(20))
						.city("no city").countryEnum(CountryEnum.KAZAKHSTAN).email("noemail@email.ru").genderEnum(GenderEnum.MALE)
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
