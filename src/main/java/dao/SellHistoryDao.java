package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;

import entity.SellHistoryEntity;
import exceptions.IncorrectQuantityException;
import utlis.ConnectionPoolManager;

public class SellHistoryDao {

	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO sell_history (item_id, login, quantity, sell_date)
			VALUES (?, ?, ?, ?);
			""";

	private static boolean insert(SellHistoryEntity sellEntity) {
		if (sellEntity.getItems().getQuantity() < sellEntity.getQuantity()) {
			throw new IncorrectQuantityException();
		}
		try (var connection = ConnectionPoolManager.get();
				var prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT)) {
			prepareStatement.setLong(1, sellEntity.getItems().getItemId());
			prepareStatement.setString(2, sellEntity.getPersonalAccount().getLogin());
			prepareStatement.setInt(3, sellEntity.getQuantity());
			if (sellEntity.getSellDate() != null) {
				prepareStatement.setTimestamp(4, Timestamp
						.valueOf(sellEntity.getSellDate().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
			} else {
				prepareStatement.setString(4, "now()");
			}
			var result = prepareStatement.executeUpdate();
			ItemsDao.changeQuantity(sellEntity.getQuantity(), sellEntity.getItems().getItemId());
			return result > 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
