package utlis;

import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.PremiumUserEntity;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
@UtilityClass
public class HibernateSessionFactory {
    public SessionFactory getSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ItemsEntity.class);
        configuration.addAnnotatedClass(PersonalAccountEntity.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(PremiumUserEntity.class);
        return configuration;
    }
}
