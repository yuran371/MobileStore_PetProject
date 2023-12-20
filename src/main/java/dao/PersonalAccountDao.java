package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dto.DtoPersonalAccount;
import entity.PersonalAccountEntity;
import utlis.ConnectionPoolManager;

public class PersonalAccountDao {

	private static PersonalAccountDao INSTANCE = new PersonalAccountDao();

	private PersonalAccountDao() {
	}

	public static PersonalAccountDao getInstance() {
		return INSTANCE;
	}

	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO personal_account (email, password, name, surname, birthday, country, city, address, phone_number, gender)
			VALUES (?, crypt(?, gen_salt('bf')), ?, ?, ?, ?, ?, ?, ?, ?);
			""";

	private final static String SQL_GET_BY_LOGIN_STATEMENT = """
			SELECT account_id, email, name, surname, country, city, address, phone_number
			FROM personal_account
			WHERE login LIKE ?;
			""";

	private final static String SQL_GET_BY_ID_STATEMENT = """
			SELECT account_id, email, name, surname, country, city, address, phone_number
			FROM personal_account
			WHERE account_id = ?;
			""";

	private final static String SQL_SELECT_STATEMENT = """
				SELECT account_id, email, name, surname, country, city, address, phone_number
				FROM personal_account
			""";

	public Optional<Long> insertAccount(PersonalAccountEntity accountEntity) {
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT,
						Statement.RETURN_GENERATED_KEYS)) {
			prepareStatement.setString(1, accountEntity.getEmail());
			prepareStatement.setString(2, accountEntity.getName());
			prepareStatement.setString(3, accountEntity.getSurname());
			prepareStatement.setString(4, accountEntity.getCountry());
			prepareStatement.setString(5, accountEntity.getCity());
			prepareStatement.setString(6, accountEntity.getAddress());
			prepareStatement.setString(7, accountEntity.getPhoneNumber());
			prepareStatement.executeUpdate();
			var generatedKeys = prepareStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return Optional.ofNullable(generatedKeys.getLong("account_id"));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			return Optional.empty();
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

	public Optional<PersonalAccountEntity> getByID(Long id) {
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

	public List<PersonalAccountEntity> sortByParams(DtoPersonalAccount filter) {
		List<String> sqlWhereStatement = new ArrayList<String>();
		List<String> parameters = new ArrayList<>();
		if (filter.city() != null) {
			sqlWhereStatement.add("city = ?");
			parameters.add(filter.city());
		}
		if (filter.country() != null) {
			sqlWhereStatement.add("country = ?");
			parameters.add(filter.country());
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
		return new PersonalAccountEntity(executeQuery.getLong("account_id"), executeQuery.getString("email"),
				executeQuery.getString("name"), executeQuery.getString("surname"), executeQuery.getString("country"),
				executeQuery.getString("city"), executeQuery.getString("address"),
				executeQuery.getString("phone_number"));
	}
}
