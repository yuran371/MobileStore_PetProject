package extentions;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import utlis.HibernateTestUtil;

import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

public class DaoTestResolver implements ParameterResolver {

    public static final Class<SessionFactory> SESSION_FACTORY_CLASS = SessionFactory.class;
    private static final Class<PersonalAccountDao> PERSONAL_ACCOUNT_DAO_CLASS = PersonalAccountDao.class;
    private static final Class<ItemsDao> ITEMS_DAO_CLASS = ItemsDao.class;
    private static final Class<SellHistoryDao> SELL_HISTORY_DAO_CLASS = SellHistoryDao.class;
    private SessionFactory sessionFactory;
    private final Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                                                                     new Class[]{Session.class},
                                                                     (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return checkType(parameterContext.getParameter());
    }

    private static boolean checkType(Parameter parameter) {
        Class<?> type = parameter.getType();
        return (type == PERSONAL_ACCOUNT_DAO_CLASS) || (type == SESSION_FACTORY_CLASS) || (type == ITEMS_DAO_CLASS) ||
                (type == SELL_HISTORY_DAO_CLASS);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Class<?> type = parameterContext.getParameter().getType();
        Store store = extensionContext
                .getStore(ExtensionContext.Namespace.create(type.getSimpleName()));
        if (type == SESSION_FACTORY_CLASS) {
            sessionFactory = HibernateTestUtil.getSessionFactory();
            return store.getOrComputeIfAbsent(SESSION_FACTORY_CLASS, t -> sessionFactory);
        } else if (type == ITEMS_DAO_CLASS) {
            return store.getOrComputeIfAbsent(ITEMS_DAO_CLASS, t -> new ItemsDao(session));
        } else if (type == PERSONAL_ACCOUNT_DAO_CLASS) {
            return store.getOrComputeIfAbsent(PERSONAL_ACCOUNT_DAO_CLASS, t -> new PersonalAccountDao(session));
        } else {
            return store.getOrComputeIfAbsent(SELL_HISTORY_DAO_CLASS, t -> new SellHistoryDao(session));
        }
    }
}