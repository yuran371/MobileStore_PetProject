package mapper;

import dao.DaoTestFields;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import dto.personalAccount.UserSellHistoryDto;
import entity.SellHistoryEntity;
import extentions.AddTestEntitiesExtension;
import extentions.DaoTestResolver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({AddTestEntitiesExtension.class, DaoTestResolver.class})
public class UserSellHistoryMapperTest extends DaoTestFields {

    public SessionFactory sessionFactory;
    public PersonalAccountDao personalAccountDao;
    public SellHistoryDao sellHistoryDao;

    public UserSellHistoryMapperTest(SessionFactory sessionFactory, PersonalAccountDao personalAccountDao, SellHistoryDao sellHistoryDao) {
        this.sessionFactory = sessionFactory;
        this.personalAccountDao = personalAccountDao;
        this.sellHistoryDao = sellHistoryDao;
    }

    @Test
    void checkMapper() {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        SellHistoryEntity sellHistoryEntity = sellHistoryDao.getById(1L).get();
        UserSellHistoryDto userSellHistoryDto = UserSellHistoryMapper.INSTANCE.sellHistoryEntityToUserSellHistoryDto(sellHistoryEntity);
        System.out.println();
        currentSession.getTransaction().commit();
    }
}
