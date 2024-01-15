package extentions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import dao.PersonalAccountDao;

public class PersonalAccountParameterResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == PersonalAccountDao.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Store store = extensionContext
				.getStore(ExtensionContext.Namespace.create(parameterContext.getParameter().getType().getSimpleName()));
		return store.getOrComputeIfAbsent(PersonalAccountDao.class, t -> PersonalAccountDao.getInstance());
	}

}
