package com.kavishka.kavishkamart.listener;

import com.kavishka.kavishkamart.util.DbUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * ServletContextListener responsible for initializing and closing HikariCP Connection Pool.
 * Enforces Mandatory Engineering Rule #5.
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);
    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing KavishkaMart Application Context & Database Pool...");

        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                logger.warn("config.properties not found on classpath, using defaults");
            }
        } catch (Exception e) {
            logger.error("Failed to load config.properties", e);
        }

        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null || dbUrl.isEmpty()) {
            dbUrl = props.getProperty("db.url", "jdbc:h2:mem:kavishkamart;DB_CLOSE_DELAY=-1;MODE=MySQL");
        }

        String dbUser = System.getenv("DB_USER");
        if (dbUser == null || dbUser.isEmpty()) {
            dbUser = props.getProperty("db.user", "sa");
        }

        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.isEmpty()) {
            dbPassword = props.getProperty("db.password", "");
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);

        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.max-size", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.min-idle", "2")));
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setPoolName("KavishkaMartHikariPool");

        try {
            dataSource = new HikariDataSource(config);
            DbUtil.setDataSource(dataSource);
            logger.info("HikariCP connection pool initialized successfully for URL: {}", dbUrl);

            // Execute Database Initializer scripts
            initDatabaseSchemaAndSeed();
        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP connection pool", e);
            throw new RuntimeException("Could not initialize HikariCP DataSource", e);
        }
    }

    private void initDatabaseSchemaAndSeed() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("Executing database schema initialization...");
            executeSqlScript(stmt, "db/schema.sql");

            logger.info("Executing seed data script...");
            executeSqlScript(stmt, "db/seed.sql");

            logger.info("Database schema and seed data initialized cleanly.");
        } catch (Exception e) {
            logger.warn("Database initialization exception (table may already exist): {}", e.getMessage());
        }
    }

    private void executeSqlScript(Statement stmt, String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                logger.warn("SQL script not found: {}", resourcePath);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append("\n");
                    if (line.endsWith(";")) {
                        String statementStr = sql.toString().replace(";", "").trim();
                        if (!statementStr.isEmpty()) {
                            try {
                                stmt.execute(statementStr);
                            } catch (Exception ex) {
                                // Ignore duplicate key / table already exists errors during seed
                                logger.debug("SQL execution notice: {}", ex.getMessage());
                            }
                        }
                        sql.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script: " + resourcePath, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down KavishkaMart Application Context & Connection Pool...");
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource closed successfully.");
        }
    }
}
