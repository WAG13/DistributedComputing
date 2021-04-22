package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseConnection {
    INSTANCE;

    private static final String DB_URI = "jdbc:postgresql://localhost:5432/worldmap";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "131313";
    private final Connection connection;

    DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URI, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
