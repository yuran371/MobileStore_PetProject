package dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import dto.PersonalAccountFilter;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.graph.GraphSemantic;

import java.util.List;
import java.util.Optional;

import static entity.QItemsEntity.itemsEntity;
import static entity.QPersonalAccountEntity.personalAccountEntity;
import static entity.QSellHistoryEntity.sellHistoryEntity;

@Slf4j
public class PersonalAccountDao extends DaoBase<Long, PersonalAccountEntity> {

    public PersonalAccountDao(EntityManager entityManager) {
        super(entityManager, PersonalAccountEntity.class);
    }

    public List<PersonalAccountEntity> getAll() {
        return new JPAQuery<PersonalAccountEntity>(getEntityManager())
                .select(personalAccountEntity)
                .from(personalAccountEntity)
                .leftJoin(personalAccountEntity.phonePurchases, sellHistoryEntity)
                .fetch();
    }

    public List<PersonalAccountEntity> getAllWithPhonePurchases() {
        var graph = getEntityManager().createEntityGraph(PersonalAccountEntity.class);
        graph.addAttributeNodes("phonePurchases");
        var subGraph = graph.addSubgraph("phonePurchases", SellHistoryEntity.class);
        subGraph.addAttributeNodes("itemId");
        return getEntityManager().createQuery("select p from PersonalAccountEntity p", PersonalAccountEntity.class)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), graph)
                .getResultList();

    }

    public Optional<PersonalAccountEntity> validateAuth(String email, String password) {
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(getEntityManager())
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email)
                                                          .and(personalAccountEntity.password.eq(password)))
                                           .fetchOne());
    }

    public Optional<PersonalAccountEntity> getByEmail(String email) {
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(getEntityManager())
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email))
                                           .fetchOne());
    }


    public List<ItemsEntity> getAllBoughtPhones(Long id) {
        return new JPAQuery<ItemsEntity>(getEntityManager())
                .select(itemsEntity)
                .from(sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .join(sellHistoryEntity.user, personalAccountEntity)
                .where(personalAccountEntity.id.eq(id))
                .fetch();
    }

    public List<Tuple> getTopTenMostSpenders() {
        return new JPAQuery<Object[]>(getEntityManager())
                .select(personalAccountEntity, itemsEntity.price.sum())
                .from(personalAccountEntity)
                .join(personalAccountEntity.phonePurchases, sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .groupBy(personalAccountEntity.id)
                .orderBy(itemsEntity.price.sum().desc())
                .fetch();
    }

    public List<PersonalAccountEntity> sortByGenderAndCountry(PersonalAccountFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getGender(), personalAccountEntity.genderEnum::eq)
                .add(filter.getCountry(), personalAccountEntity.countryEnum::eq)
                .buildAnd();
        return new JPAQuery<PersonalAccountEntity>(getEntityManager())
                .select(personalAccountEntity)
                .from(personalAccountEntity)
                .where(predicate)
                .fetch();
    }

}
