package utlis.jdbc;

import exceptions.DriverClassLoadException;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public final class ConnectionPoolManager {

    private static final String SQL_USER_KEY = "db.user";
    private static final String SQL_PASSWORD_KEY = "db.password";
    private static final String SQL_URL_KEY = "db.url";
    private static final String SQL_CONNECTION_POOL_SIZE = "db.pool.size";
    private static ArrayBlockingQueue<Connection> connectionPool;
    private static List<Connection> sourceConnections;

    static {
        load();
        createConnectionPool();

    }

    private ConnectionPoolManager() {
    }

    private static void createConnectionPool() {
        String poolSize = PropertiesUtil.getProperty(SQL_CONNECTION_POOL_SIZE);
        int size = poolSize == null ? 10 : Integer.parseInt(poolSize);
        connectionPool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            var connection = open();
            var newProxyInstance = (Connection) Proxy.newProxyInstance(ConnectionPoolManager.class.getClassLoader(),
                                                                       new Class[]{Connection.class},
                                                                       (proxy, method, args) -> method.getName()
                                                                               .equals("close") ?
                                                                               connectionPool.add((Connection) proxy)
                                                                               : method.invoke(connection, args));
            connectionPool.add(newProxyInstance);
            sourceConnections.add(connection);
        }
    }

    private static void load() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverClassLoadException(e);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.getProperty(SQL_URL_KEY),
                                               PropertiesUtil.getProperty(SQL_USER_KEY),
                                               PropertiesUtil.getProperty(SQL_PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        for (Connection connection : sourceConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
