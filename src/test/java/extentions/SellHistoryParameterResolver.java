package extentions;

import dao.SellHistoryDao;
import org.hibernate.Session;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SellHistoryParameterResolver implements ParameterResolver {

	Session session;
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == SellHistoryDao.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Store store = extensionContext
				.getStore(ExtensionContext.Namespace.create(parameterContext.getParameter().getType().getSimpleName()));
		return store.getOrComputeIfAbsent(SellHistoryDao.class, t -> new SellHistoryDao(session));
	}

}
