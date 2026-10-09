package com.kavishka.kavishkamart.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility for accessing database connections from the application HikariCP DataSource.
 */
public class DbUtil {
    private static DataSource dataSource;

    private DbUtil() {
    }

    public static void setDataSource(DataSource ds) {
        dataSource = ds;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Obtains a JDBC Connection from the HikariCP DataSource.
     * Enforces Mandatory Rule #5 (No DriverManager.getConnection calls).
     *
     * @return Connection instance
     * @throws SQLException if connection cannot be acquired
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource has not been initialized by AppContextListener");
        }
        return dataSource.getConnection();
    }
}
