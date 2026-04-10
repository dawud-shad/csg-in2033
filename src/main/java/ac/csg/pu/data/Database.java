package ac.csg.pu.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final static Logger logger = LoggerFactory.getLogger(Database.class);

    private static final String DB_DIR = System.getProperty("user.dir") + "/data";

    public static Connection connect(String dbName) throws SQLException {
        // Define database directory
        File dir = new File(DB_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            logger.error("Failed to create database directory: {}", dir.getAbsolutePath());
            throw new SQLException("Cannot create database directory: " + dir.getAbsolutePath());
        }

        // Create database file path
        File dbFile = new File(dir, dbName);

        // Connect to user database
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        logger.info("Database URL of name {}: {}", dbName, url);

        return DriverManager.getConnection(url);
    }
}