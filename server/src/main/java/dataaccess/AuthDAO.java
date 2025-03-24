package dataaccess;

import chess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    AuthData getAuthData(String token) throws DataAccessException;

    void addAuthData(AuthData authData) throws DataAccessException;

    void removeAuthData(String token) throws DataAccessException;

    void removeAllAuthData() throws DataAccessException;
}
