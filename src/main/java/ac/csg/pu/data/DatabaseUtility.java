package ac.csg.pu.data;

import ac.csg.pu.data.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DatabaseUtility {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtility.class);
    private final String dbName;

    public DatabaseUtility(String dbName) { this.dbName = dbName; }

    // Execute INSERT/UPDATE/DELETE
    public void executeUpdate(String sql, Object... params) {
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("DB update failed: {}", sql, e);
        }
    }

    public int executeInsert(String sql, Object... params) {
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("DB insert failed: {}", sql, e);
        }
        return -1;
    }

    // Generic query single type
    public <T> T querySingle(String sql, ResultSetMapper<T> mapper, Object... params) {
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper.map(rs);
            }

        } catch (Exception e) {
            logger.error("DB query failed: {}", sql, e);
        }
        return null;
    }

    // Generic query list
    public <T> List<T> queryMultiple(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }

        } catch (Exception e) {
            logger.error("DB query failed: {}", sql, e);
        }
        return results;
    }

    // Result set -> object (1:1)
    // Provided by user
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws Exception;
    }

    // Convenience wrappers
    public int queryInt(String sql, Object... params) {
        Integer result = querySingle(sql, rs -> {
            try { return rs.getInt(1); }
            catch (Exception e) { return 0; }
        }, params);
        return result != null ? result : 0;
    }

    public String queryString(String sql, Object... params) {
        return querySingle(sql, rs -> {
            try { return rs.getString(1); }
            catch (Exception e) { return null; }
        }, params);
    }

    public double queryDouble(String sql, Object... params) {
        Double result = querySingle(sql, rs -> {
            try { return rs.getDouble(1); }
            catch (Exception e) { return 0.0; }
        }, params);
        return result != null ? result : 0.0;
    }

    public boolean queryBoolean(String sql, Object... params) {
        Boolean result = querySingle(sql, rs -> {
            try { return rs.getInt(1) != 0; }
            catch (Exception e) { return false; }
        }, params);
        return result != null ? result : false;
    }

    // DEPRECATED METHODS, DO NOT USE

    /*
    // Query single integer
    public static int queryInt(String sql, Object... params) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("DB query failed: {}", sql, e);
        }
        return 0;
    }

    // Query single string
    public static String queryString(String sql, Object... params) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            logger.error("DB query failed: {}", sql, e);
        }
        return null;
    }
     */
}
