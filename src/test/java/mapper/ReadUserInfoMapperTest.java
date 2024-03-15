package mapper;

import dao.DaoTestFields;
import dao.PersonalAccountDao;
import dto.personalAccount.ReadUserInfoDto;
import entity.PersonalAccountEntity;
import extentions.AddTestEntitiesExtension;
import extentions.DaoTestResolver;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({AddTestEntitiesExtension.class, DaoTestResolver.class})
public class ReadUserInfoMapperTest extends DaoTestFields {

    public SessionFactory sessionFactory;
    public PersonalAccountDao personalAccountDao;

    public ReadUserInfoMapperTest(SessionFactory sessionFactory, PersonalAccountDao personalAccountDao) {
        this.sessionFactory = sessionFactory;
        this.personalAccountDao = personalAccountDao;
    }

    @Test
    void checkMapper() {
        @Cleanup Session currentSession = sessionFactory.openSession();
        currentSession.beginTransaction();
        Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJakartaHintName(),
                                                currentSession.getEntityGraph("withItems"));
        PersonalAccountEntity personalAccountEntity = currentSession.find(PersonalAccountEntity.class, 1L, properties);
        ReadUserInfoDto userInfoDto = ReadUserInfoMapper.INSTANCE.personalAccountEntityToUserInfoDto(personalAccountEntity);
        System.out.println();
        currentSession.getTransaction().commit();
    }

}
