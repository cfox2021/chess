package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Could Not Configure Database: from USER DAO");
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void addUserData(UserData userData) throws DataAccessException {

    }

    @Override
    public void removeAllUserData() {

    }
}
