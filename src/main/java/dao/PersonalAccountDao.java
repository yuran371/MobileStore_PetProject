package dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import dto.PersonalAccountFilter;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import entity.enums.DiscountEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import utlis.SqlExceptionLogger;

import java.util.List;
import java.util.Optional;

import static entity.QItemsEntity.itemsEntity;
import static entity.QPersonalAccountEntity.personalAccountEntity;
import static entity.QPremiumUserEntity.premiumUserEntity;
import static entity.QSellHistoryEntity.sellHistoryEntity;

@Slf4j
public class PersonalAccountDao  {

    private static final PersonalAccountDao INSTANCE = new PersonalAccountDao();

    private static final SqlExceptionLogger SQL_EXCEPTION_LOGGER = SqlExceptionLogger.getInstance();

    private PersonalAccountDao() {
    }

    public static PersonalAccountDao getInstance() {
        return INSTANCE;
    }

    public Optional<Long> insert(@NonNull PersonalAccountEntity accountEntity, Session session) {
        session.beginTransaction();
        session.persist(accountEntity);
        session.getTransaction().commit();
        return Optional.ofNullable(accountEntity.getId());
    }

    public Optional<PersonalAccountEntity> getById(Long id, Session session) {
        return Optional.ofNullable(session.get(PersonalAccountEntity.class, id));
    }


    public List<PersonalAccountEntity> getAll(Session session) {
        return new JPAQuery<PersonalAccountEntity>(session)
                .select(personalAccountEntity)
                .from(personalAccountEntity)
                .leftJoin(personalAccountEntity.phonePurchases, sellHistoryEntity)
                .fetch();
    }

    public List<PersonalAccountEntity> getAllWithPhonePurchases(Session session) {
        var graph = session.createEntityGraph(PersonalAccountEntity.class);
        graph.addAttributeNodes("phonePurchases");
        var subGraph = graph.addSubgraph("phonePurchases", SellHistoryEntity.class);
        subGraph.addAttributeNodes("itemId");
        return session.createQuery("select p from PersonalAccountEntity p", PersonalAccountEntity.class)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), graph)
                .list();

    }

    public boolean delete(PersonalAccountEntity account, Session session) {
        if (account.getId() == null) {
            return false;
        }
        session.beginTransaction();
        session.remove(account);
        var deleteResult = session.get(PersonalAccountEntity.class, account.getId());
        session.getTransaction().commit();
        return deleteResult == null;
    }

    public Optional<PersonalAccountEntity> validateAuth(String email, String password, Session session) {
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(session)
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email)
                                                          .and(personalAccountEntity.password.eq(password)))
                                           .fetchOne());
    }

    public Optional<PersonalAccountEntity> getByEmail(String email, Session session) {
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(session)
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email))
                                           .fetchOne());
    }

    public Optional<DiscountEnum> checkDiscount(Long id, Session session) {
        return Optional.ofNullable(new JPAQuery<DiscountEnum>(session)
                                           .select(premiumUserEntity.discountEnum)
                                           .from(premiumUserEntity)
                                           .where(premiumUserEntity.id.eq(id))
                                           .fetchOne());
    }

    public List<ItemsEntity> getAllBoughtPhones(Long id, Session session) {
        return new JPAQuery<ItemsEntity>(session)
                .select(itemsEntity)
                .from(sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .join(sellHistoryEntity.user, personalAccountEntity)
                .where(personalAccountEntity.id.eq(id))
                .fetch();
    }

    public List<Tuple> getTopTenMostSpenders(Session session) {
        return new JPAQuery<Object[]>(session)
                .select(personalAccountEntity, itemsEntity.price.sum())
                .from(personalAccountEntity)
                .join(personalAccountEntity.phonePurchases, sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .groupBy(personalAccountEntity.id)
                .orderBy(itemsEntity.price.sum().desc())
                .fetch();
    }

        public List<PersonalAccountEntity> sortByGenderAndCountry(PersonalAccountFilter filter, Session session) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getGender(), personalAccountEntity.genderEnum::eq)
                .add(filter.getCountry(), personalAccountEntity.countryEnum::eq)
                .buildAnd();
        return new JPAQuery<PersonalAccountEntity>(session)
                .select(personalAccountEntity)
                .from(personalAccountEntity)
                .where(predicate)
                .fetch();
    }

}
