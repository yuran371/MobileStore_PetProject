package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entity.ItemsEntity;
import utlis.ConnectionPoolManager;
import utlis.SqlExceptionLogger;

public class ItemsDao implements Dao<Long, ItemsEntity> {

	private static ItemsDao INSTANCE = new ItemsDao();

	private static SqlExceptionLogger SQL_EXCEPTION_LOGGER = SqlExceptionLogger.getInstance();

	public static ItemsDao getInstance() {
		return INSTANCE;
	}

	private ItemsDao() {
	};

	private final static Integer DEFAULT_COLUMNS_QT_IN_ITEMS_TABLE = 6;
	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO items (model, brand, attributes, price, currency, quantity)
			""";
	private final static String SQL_CHANGE_QUANTITY = """
			UPDATE items
			SET quantity=quantity-?
	WHERE item_id=?
			""";
	private final static String SQL_GET_STATEMENT = """
			SELECT *
			FROM items
			WHERE item_id = ?
			""";
	private final static String SQL_FIND_ALL = """
			SELECT *
			FROM items
			""";

	private final static String SQL_DELETE_BY_ID = """
			DELETE FROM items
			WHERE item_id = ?
			""";

	private final static String SQL_FIND_BY_BRAND = """
			SELECT *
			FROM items
			WHERE brand = ?
			""";

	public Long Insert(List<ItemsEntity> arrayList) {
		String innerSql = "(?, ?, ?, ?, ?, ?)";
		ArrayList<String> valuesList = new ArrayList<String>();
		for (int i = 0; i < arrayList.size(); i++) {
			valuesList.add(innerSql);
		}
		String collect = valuesList.stream().collect(Collectors.joining(", ", "VALUES ", ";"));

//		System.out.println(valuesList);

		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT + collect,
						Statement.RETURN_GENERATED_KEYS)) {
//			System.out.println(SQL_INSERT_STATEMENT + collect);
			int qt = DEFAULT_COLUMNS_QT_IN_ITEMS_TABLE; // qt - quantity
			for (int i = 0; i < arrayList.size(); i++) {
				prepareStatement.setString(qt * i + 1, arrayList.get(i).getModel());
				prepareStatement.setString(qt * i + 2, arrayList.get(i).getBrand());
				prepareStatement.setString(qt * i + 3, arrayList.get(i).getAttributes());
				prepareStatement.setDouble(qt * i + 4, arrayList.get(i).getPrice());
				prepareStatement.setString(qt * i + 5, arrayList.get(i).getCurrency());
				prepareStatement.setInt(qt * i + 6, arrayList.get(i).getQuantity());
			}
			int executeUpdate = prepareStatement.executeUpdate();
			ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
			while (generatedKeys.next()) {
				System.out.println(generatedKeys.getLong("item_id"));
			}
			return (long) executeUpdate;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<ItemsEntity> findAll() {
		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(SQL_FIND_ALL)) {
			ResultSet resultSet = prepareStatement.executeQuery();
			List<ItemsEntity> itemsList = new ArrayList<>();
			while (resultSet.next()) {
				itemsList.add(buildItems(resultSet));
			}
			return itemsList;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public Optional<ItemsEntity> getById(Long itemId) {
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_GET_STATEMENT)) {
			prepareStatement.setLong(1, itemId);
			var resultSet = prepareStatement.executeQuery();
			ItemsEntity entityResult = null;
			while (resultSet.next()) {
				entityResult = buildItems(resultSet);
			}
			return Optional.ofNullable(entityResult);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Long insert(ItemsEntity entity) {
		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_STATEMENT,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, entity.getModel());
			statement.setString(2, entity.getBrand());
			statement.setString(3, entity.getAttributes());
			statement.setDouble(4, entity.getPrice());
			statement.setString(5, entity.getCurrency());
			statement.setInt(6, entity.getQuantity());
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong("account_id");
			}
		} catch (SQLException e) {
			SQL_EXCEPTION_LOGGER.addException(e);
		}
		return 0l;
	}

	@Override
	public boolean delete(Long params) {
		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
			statement.setLong(1, params);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SQL_EXCEPTION_LOGGER.addException(e);
		}
		return false;
	}

	public Integer changeQuantity(int quantity, long itemId) {
		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(SQL_CHANGE_QUANTITY,
						Statement.RETURN_GENERATED_KEYS)) {
			prepareStatement.setInt(1, quantity);
			prepareStatement.setLong(2, itemId);
			prepareStatement.executeUpdate();
			ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt("quantity");
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public List<ItemsEntity> findByBrand(String brand) {
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_FIND_BY_BRAND)) {
			prepareStatement.setString(1, brand);
			var resultSet = prepareStatement.executeQuery();
			List<ItemsEntity> itemsList = new ArrayList<>();
			while (resultSet.next()) {
				itemsList.add(buildItems(resultSet));
			}
			return itemsList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private ItemsEntity buildItems(ResultSet resultSet) throws SQLException {
		return new ItemsEntity(resultSet.getLong("item_id"), resultSet.getString("model"), resultSet.getString("brand"),
				resultSet.getString("attributes"), resultSet.getDouble("price"), resultSet.getString("currency"),
				resultSet.getInt("quantity"));
	}

}
