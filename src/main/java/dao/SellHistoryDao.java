package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dto.DtoSellHistoryFilter;
import entity.SellHistoryEntity;
import exceptions.IncorrectQuantityException;
import utlis.ConnectionPoolManager;

public class SellHistoryDao {

	private static ItemsDao itemsDao = ItemsDao.getInstance();

	private static PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();

	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO sell_history (item_id, login, quantity, sell_date)
			VALUES (?, ?, ?, ?);
			""";
	private final static String SQL_GET_STATEMENT = """
			SELECT sell_id, item_id, login, quantity, sell_date
			FROM sell_history
			""";

	public static boolean insert(SellHistoryEntity sellEntity) {
		if (sellEntity.getItems().getQuantity() < sellEntity.getQuantity()) {
			throw new IncorrectQuantityException();
		}
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT)) {
			prepareStatement.setLong(1, sellEntity.getItems().getItemId());
			prepareStatement.setString(2, sellEntity.getPersonalAccount().getEmail());
			prepareStatement.setInt(3, sellEntity.getQuantity());
			if (sellEntity.getSellDate() != null) {
				prepareStatement.setTimestamp(4, Timestamp.valueOf(sellEntity.getSellDate().toLocalDateTime()));
			} else {
				prepareStatement.setString(4, "now()");
			}
			var result = prepareStatement.executeUpdate();
			itemsDao.changeQuantity(sellEntity.getQuantity(), sellEntity.getItems().getItemId());
			return result > 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<SellHistoryEntity> getWithFilter(DtoSellHistoryFilter filter) {
		List<String> statements = new ArrayList<>();
		List<Object> filters = new ArrayList<>();
		if (filter.items().getItemId() != null) {
			filters.add(filter.items().getItemId());
			statements.add("item_id = ?");
		}
		if (filter.personalAccount().getEmail() != null) {
			filters.add(filter.personalAccount().getEmail());
			statements.add("login IS LIKE %?%");
		}
		if (filter.quantity() != null) {
			filters.add(filter.quantity());
			statements.add("quantity = ?");
		}
		String SQL_WITH_FILTERS = SQL_GET_STATEMENT
				+ statements.stream().collect(Collectors.joining(" AND ", "WHERE ", ";"));
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_WITH_FILTERS)) {
			for (int i = 0; i < filters.size(); i++) {
				prepareStatement.setObject(i + 1, filters.get(i));
			}
			var resultSet = prepareStatement.executeQuery();
			List<SellHistoryEntity> result = new ArrayList<>();
			while (resultSet.next()) {
				result.add(createSellHistoryEntityFromResultSet(resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private SellHistoryEntity createSellHistoryEntityFromResultSet(ResultSet resultSet) throws SQLException {
		return new SellHistoryEntity(itemsDao.getById(resultSet.getLong("item_id")).orElseThrow(),
				personalAccountDao.getByLogin(resultSet.getString("login")).orElseThrow(), resultSet.getInt("quantity"),
				OffsetDateTime.ofInstant(Instant.ofEpochMilli(resultSet.getTimestamp("sell_date").getTime()),
						ZoneOffset.UTC));
	}
}
