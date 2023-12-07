package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.ItemsEntity;
import utlis.ConnectionPoolManager;

public class ItemsDao {
	private final static Integer DEFAULT_COLUMNS_QT_IN_ITEMS_TABLE = 5;
	private final static String SQL_INSERT_STATEMENT = """
			INSERT INTO items (model, brand, attributes, price, quantity)
			""";

	public static int Insert(List<ItemsEntity> arrayList) {
		String innerSql = "(?, ?, ?, ?, ?)";
		ArrayList<String> valuesList = new ArrayList<String>();
		for (int i = 0; i < arrayList.size(); i++) {
			valuesList.add(innerSql);
		}
		String collect = valuesList.stream().collect(Collectors.joining(", ", "VALUES ", ";"));
		
		System.out.println(valuesList);

		try (Connection connection = ConnectionPoolManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(SQL_INSERT_STATEMENT + collect,
						Statement.RETURN_GENERATED_KEYS)) {
			System.out.println(SQL_INSERT_STATEMENT + collect);
			int qt = DEFAULT_COLUMNS_QT_IN_ITEMS_TABLE; // qt - quantity
			for (int i = 0; i < arrayList.size(); i++) {
				prepareStatement.setString(qt * i + 1, arrayList.get(i).getModel());
				prepareStatement.setString(qt * i + 2, arrayList.get(i).getBrand());
				prepareStatement.setString(qt * i + 3, arrayList.get(i).getAttributes());
				prepareStatement.setDouble(qt * i + 4, arrayList.get(i).getPrice());
				prepareStatement.setInt(qt * i + 5, arrayList.get(i).getQuantity());
			}
			int executeUpdate = prepareStatement.executeUpdate();
			ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
			while(generatedKeys.next()) {
				System.out.println(generatedKeys.getLong("item_id"));
			}
			return executeUpdate;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
