package com.kavishka.util;

import com.kavishka.kavishkamart.util.DbUtil;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class for obtaining JDBC connections from the application's HikariCP pool.
 */
public class DatabaseUtil {

    public static Connection getConnection() throws SQLException {
        return DbUtil.getConnection();
    }
}

