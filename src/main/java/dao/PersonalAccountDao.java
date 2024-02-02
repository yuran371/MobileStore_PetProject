package dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import dto.CreateAccountDto;
import entity.*;
import entity.enums.DiscountEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import utlis.ConnectionPoolManager;
import utlis.HibernateSessionFactory;
import utlis.SqlExceptionLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static entity.QItemsEntity.*;
import static entity.QPersonalAccountEntity.personalAccountEntity;
import static entity.QPremiumUserEntity.premiumUserEntity;
import static entity.QSellHistoryEntity.*;

@Slf4j
public class PersonalAccountDao implements Dao<Long, PersonalAccountEntity> {

    private static final PersonalAccountDao INSTANCE = new PersonalAccountDao();

    private static final SqlExceptionLogger SQL_EXCEPTION_LOGGER = SqlExceptionLogger.getInstance();

    private PersonalAccountDao() {
    }

    public static PersonalAccountDao getInstance() {
        return INSTANCE;
    }

    private final static String SQL_DELETE_BY_ID = """
            DELETE FROM personal_account
            WHERE account_id = ?
            """;

    @Override
    public Optional<Long> insert(@NonNull PersonalAccountEntity accountEntity) {
        try (SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(accountEntity);
            transaction.commit();
            log.info("User {} successfully added", accountEntity);
            return Optional.ofNullable(accountEntity.getId());
        } catch (ConstraintViolationException constraintViolationException) {
            log.info("New user not added. User with {} email already exist in database", accountEntity.getEmail());
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersonalAccountEntity> getById(Long id) {
        return Optional.empty();
    }

    public Optional<PersonalAccountEntity> getById(Long id, Session session) {
        return Optional.ofNullable(session.get(PersonalAccountEntity.class, id));
    }

    @Override
    public List<PersonalAccountEntity> findAll() {
        return null;
    }

    @Override
    public boolean delete(Long params) {
        try (Connection connection = ConnectionPoolManager.get();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setLong(1, params);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            SQL_EXCEPTION_LOGGER.addException(e);
        }
        return false;
    }

    public Optional<PersonalAccountEntity> validateAuth(String email, String password, Session session) {
//        return session.createQuery("select p from PersonalAccountEntity p " +
//                                           "where p.email = :email and p.password = :password",
//                                   PersonalAccountEntity.class)
//                .setParameter("email", email)
//                .setParameter("password", password)
//                .uniqueResultOptional();
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(session)
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email)
                                                          .and(personalAccountEntity.password.eq(password)))
                                           .fetchOne());
    }

    public Optional<PersonalAccountEntity> getByEmail(String email, Session session) {
//        return session.createQuery("select p from PersonalAccountEntity p where p.email = :email",
//                                   PersonalAccountEntity.class)
//                .setParameter("email", email)
//                .uniqueResultOptional();
        return Optional.ofNullable(new JPAQuery<PersonalAccountEntity>(session)
                                           .select(personalAccountEntity)
                                           .from(personalAccountEntity)
                                           .where(personalAccountEntity.email.eq(email))
                                           .fetchOne());
    }

    public Optional<DiscountEnum> checkDiscount(Long id, Session session) {
//        return session.createQuery("select p.discount from PremiumUserEntity p where p.id = :id", Discount.class)
//                .setParameter("id", id).uniqueResultOptional();
        return Optional.ofNullable(new JPAQuery<DiscountEnum>(session)
                                           .select(premiumUserEntity.discountEnum)
                                           .from(premiumUserEntity)
                                           .where(premiumUserEntity.id.eq(id))
                                           .fetchOne());
    }

    public List<ItemsEntity> getAllBoughtPhones(Long id, Session session) {
//        return session.createQuery("select i from SellHistoryEntity s " +
//                                           "join s.itemId i " +
//                                           "where s.user.id = :id", ItemsEntity.class)
//                .setParameter("id", id)
//                .list();
        return new JPAQuery<ItemsEntity>(session)
                .select(itemsEntity)
                .from(sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .join(sellHistoryEntity.user, personalAccountEntity)
                .where(personalAccountEntity.id.eq(id))
                .fetch();
    }

    public List<Tuple> getTopTenMostSpenders(Session session) {
//        return session.createQuery("select p, sum(i.price) from PersonalAccountEntity p " +
//                                           "join p.orders s " +
//                                           "join s.itemId i " +
//                                           "group by p.id " +
//                                           "order by sum(i.price) DESC", Object[].class)
//                .setMaxResults(10)
//                .list();
        return new JPAQuery<Object[]>(session)
                .select(personalAccountEntity, itemsEntity.price.sum())
                .from(personalAccountEntity)
                .join(personalAccountEntity.orders, sellHistoryEntity)
                .join(sellHistoryEntity.itemId, itemsEntity)
                .groupBy(personalAccountEntity.id)
                .orderBy(itemsEntity.price.sum().desc())
                .fetch();
    }

    public List<PersonalAccountEntity> sortByParams(CreateAccountDto filter) {

        return new ArrayList<>();
    }

}
