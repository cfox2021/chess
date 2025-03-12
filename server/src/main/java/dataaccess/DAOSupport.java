package dataaccess;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public interface DAOSupport {
    default void configureDatabase(String[] createStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Could Not Configure Data");
        }
    }

    default int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            boolean isInsert = statement.trim().toUpperCase().startsWith("INSERT");
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param == null) {
                        throw new DataAccessException("Provided Data missing parameter: " + statement);
                    }
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else {
                        ps.setObject(i + 1, param); // Handles JSON and other types
                    }
                }
                ps.executeUpdate();
                if (isInsert) {
                    var rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could Not Update Data");
        }
    }
}
