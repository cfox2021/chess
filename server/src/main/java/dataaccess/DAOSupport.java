package dataaccess;

import java.sql.SQLException;

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

    default void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param == null) {
                        throw new DataAccessException("Provided Data missing parameter: " + statement);
                    }
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could Not Update Data");
        }
    }
}
