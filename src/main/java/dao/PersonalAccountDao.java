package dao;

import dto.CreateAccountDto;
import entity.Country;
import entity.PersonalAccountEntity;
import lombok.NonNull;
import lombok.SneakyThrows;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
public class PersonalAccountDao implements Dao<Long, PersonalAccountEntity> {

    private static PersonalAccountDao INSTANCE = new PersonalAccountDao();

    private static SqlExceptionLogger SQL_EXCEPTION_LOGGER = SqlExceptionLogger.getInstance();

    private PersonalAccountDao() {
    }

    public static PersonalAccountDao getInstance() {
        return INSTANCE;
    }

    private final static String SQL_GET_BY_LOGIN_STATEMENT = """
            SELECT account_id, email, name, surname, country, city, address, phone_number
            FROM personal_account
            WHERE email LIKE ?;
            """;

    private final static String SQL_GET_BY_ID_STATEMENT = """
            SELECT account_id, email, password, name, surname, birthday, country, city, address, phone_number, gender
            FROM personal_account
            WHERE account_id = ?;
            """;

    private final static String SQL_GET_BY_EMAIL_AND_PASSWORD = """
            SELECT account_id, email, name, surname, birthday, country, city, address, phone_number, gender, image
            FROM personal_account
            WHERE email = ? AND password = crypt(?, password);
            """;

    private final static String SQL_SELECT_STATEMENT = """
            	SELECT account_id, email, name, surname, country, city, address, phone_number
            	FROM personal_account
            """;

    private final static String SQL_DELETE_BY_ID = """
            DELETE FROM personal_account
            WHERE account_id = ?
            """;

    @Override
    public Long insert(@NonNull PersonalAccountEntity accountEntity) {
        try (SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(accountEntity);
            transaction.commit();
            log.info("User {} successfully added", accountEntity);
            return accountEntity.getAccountId();
        }
        catch (ConstraintViolationException constraintViolationException) {
            log.info("New user not added. User with {} email already exist in database", accountEntity.getEmail());
            return null;
        }
    }

    @Override
    public Optional<PersonalAccountEntity> getById(Long id) {
        try (var connection = ConnectionPoolManager.get();
             var prepareStatement = connection.prepareStatement(SQL_GET_BY_ID_STATEMENT)) {
            prepareStatement.setLong(1, id);
            var executeQuery = prepareStatement.executeQuery();
            PersonalAccountEntity resultEntity = null;
            while (executeQuery.next()) {
                resultEntity = createEntityByResultSet(executeQuery);
            }
            return Optional.ofNullable(resultEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @SneakyThrows
    public Optional<PersonalAccountEntity> getByPasswordAndLogin(String login, String password) {
        try (Connection connection = ConnectionPoolManager.get();
             PreparedStatement prepareStatement = connection.prepareStatement(SQL_GET_BY_EMAIL_AND_PASSWORD)) {
            prepareStatement.setString(1, login);
            prepareStatement.setString(2, password);
            ResultSet resultSet = prepareStatement.executeQuery();
            PersonalAccountEntity result = null;
            if (resultSet.next()) {
                result = readUser(resultSet);
            }
            return Optional.ofNullable(result);
        }
    }

    public Optional<PersonalAccountEntity> getByLogin(String login) {
        try (var connection = ConnectionPoolManager.get();
             var prepareStatement = connection.prepareStatement(SQL_GET_BY_LOGIN_STATEMENT)) {
            prepareStatement.setString(1, login);
            var executeQuery = prepareStatement.executeQuery();
            PersonalAccountEntity resultEntity = null;
            while (executeQuery.next()) {
                resultEntity = createEntityByResultSet(executeQuery);
            }
            return Optional.ofNullable(resultEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PersonalAccountEntity> sortByParams(CreateAccountDto filter) {
        List<String> sqlWhereStatement = new ArrayList<String>();
        List<String> parameters = new ArrayList<>();
        if (filter.getCity() != null) {
            sqlWhereStatement.add("city = ?");
            parameters.add(filter.getCity());
        }
        if (filter.getCountry() != null) {
            sqlWhereStatement.add("country = ?");
            parameters.add(filter.getCountry());
        }

        var whereSql = sqlWhereStatement.stream().collect(Collectors.joining(" AND ", "\nWHERE ", ";"));
        try (var connection = ConnectionPoolManager.get();
             var prepareStatement = connection.prepareStatement(SQL_SELECT_STATEMENT + whereSql)) {
            for (int i = 0; i < parameters.size(); i++) {
                prepareStatement.setObject(i + 1, parameters.get(i));
            }
            var executeQuery = prepareStatement.executeQuery();
            List<PersonalAccountEntity> resultList = new ArrayList<>();
            while (executeQuery.next()) {
                resultList.add(createEntityByResultSet(executeQuery));
            }
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static PersonalAccountEntity createEntityByResultSet(ResultSet executeQuery) throws SQLException {
        return PersonalAccountEntity.builder().accountId(executeQuery.getLong("account_id"))
                .email(executeQuery.getString("email")).build();
    }

    private PersonalAccountEntity readUser(ResultSet resultSet) throws SQLException {
        return PersonalAccountEntity.builder().email(resultSet.getObject("email", String.class))
                .name(resultSet.getObject("name", String.class)).surname(resultSet.getObject("surname", String.class))
                .image(resultSet.getObject("image", String.class)).birthday(resultSet.getDate("birthday").toLocalDate())
                .country(Country.getValue(resultSet.getObject("country", String.class)))
                .city(resultSet.getObject("city", String.class)).build();
    }
}
