package ac.csg.pu.members;

import static ac.csg.pu.data.DatabaseUtility.*;

import ac.csg.pu.data.DatabaseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDatabase {
    private final static Logger logger = LoggerFactory.getLogger(UserDatabase.class);
    private static final DatabaseUtility db = new DatabaseUtility("users.db");

    // create user table
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "email TEXT UNIQUE NOT NULL,"
                + "password TEXT NOT NULL,"
                + "userType TEXT NOT NULL,"
                + "firstLogin INTEGER DEFAULT 1,"
                + "purchaseCount INTEGER DEFAULT 0"
                + ");";
        db.executeUpdate(sql);
        logger.info("Users table created");
    }

    public static void insertUser(String email, String password, String userType) {
        String sql = "INSERT OR IGNORE INTO users(email, password, userType) VALUES(?,?,?)";
        db.executeUpdate(sql, email, password, userType);
        logger.info("User added: {}", email);
    }

    public static void insertAdmin(String email, String password) {
        insertUser(email, password, "A");
        setFirstLogin(email, false);
    }

    public static boolean login(String email, String password) {
        String sql = "SELECT 1 FROM users WHERE email=? AND password=?";
        return db.queryInt(sql, email, password) > 0;
    }

    public static String getUserType(String email) {
        String sql = "SELECT userType FROM users WHERE email=?";
        return db.queryString(sql, email);
    }

    public static void setFirstLogin(String email, boolean input) {
        db.executeUpdate("UPDATE users SET firstLogin=? WHERE email=?", input ? 1 : 0, email);
    }

    public static boolean isFirstLogin(String email) {
        return db.queryInt("SELECT firstLogin FROM users WHERE email=?", email) == 1;
    }

    public static int getPurchaseCount(String email) {
        return db.queryInt("SELECT purchaseCount FROM users WHERE email=?", email);
    }

    public static boolean isTenthOrder(String email) {
        int count = getPurchaseCount(email);
        return (count + 1) % 10 == 0;
    }

    public static void incrementPurchase(String email) {
        db.executeUpdate("UPDATE users SET purchaseCount = purchaseCount + 1 WHERE email=?", email);
    }

    public static String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}