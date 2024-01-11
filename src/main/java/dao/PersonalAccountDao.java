package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dto.CreateAccountDto;
import entity.Country;
import entity.PersonalAccountEntity;
import lombok.NonNull;
import lombok.SneakyThrows;
import utlis.ConnectionPoolManager;
import utlis.SqlExceptionLogger;

public class PersonalAccountDao implements Dao<Long, PersonalAccountEntity> {

	private static PersonalAccountDao INSTANCE = new PersonalAccountDao();

	private static SqlExceptionLogger SQL_EXCEPTION_LOGGER = SqlExceptionLogger.getInstance();

	private PersonalAccountDao() {
	}

	public static PersonalAccountDao getInstance() {
		return INSTANCE;
	}

	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO personal_account (email, password, name, surname, image, birthday, country, city, address, phone_number, gender)
			VALUES (?, crypt(?, gen_salt('bf')), ?, ?, ?, ?, ?, ?, ?, ?, ?);
			""";

	private final static String SQL_GET_BY_LOGIN_STATEMENT = """
			SELECT account_id, email, name, surname, country, city, address, phone_number
			FROM personal_account
			WHERE login LIKE ?;
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
			DELETE FROM items
			WHERE item_id = ?
			""";

	@Override
	public Long insert(@NonNull PersonalAccountEntity accountEntity) {
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT,
						Statement.RETURN_GENERATED_KEYS)) {
			prepareStatement.setString(1, accountEntity.getEmail());
			prepareStatement.setString(2, accountEntity.getPassword());
			prepareStatement.setString(3, accountEntity.getName());
			prepareStatement.setString(4, accountEntity.getSurname());
			prepareStatement.setString(5, accountEntity.getImage());
			prepareStatement.setDate(6, Date.valueOf(accountEntity.getBirthday()));
			prepareStatement.setString(7, accountEntity.getCountry().name());
			prepareStatement.setString(8, accountEntity.getCity());
			prepareStatement.setString(9, accountEntity.getAddress());
			prepareStatement.setString(10, accountEntity.getPhoneNumber());
			prepareStatement.setString(11, accountEntity.getGender().name());
			prepareStatement.executeUpdate();
			var generatedKeys = prepareStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong("account_id");
			}

		} catch (SQLException e) {
			SQL_EXCEPTION_LOGGER.addException(e);
		}
		return 0L;
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

	@Deprecated
	private static PersonalAccountEntity createEntityByResultSet(ResultSet executeQuery) throws SQLException {
		// TODO wrong realization. Need to think about security problems when getting
		// password from DB. Maybe dont save it in entity. Making Deprecated until find
		// solution.
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
