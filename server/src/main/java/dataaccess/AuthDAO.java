package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuthData(String userName) throws DataAccessException;

    void addAuthData(AuthData authData) throws DataAccessException;

    void removeAuthData(String username) throws DataAccessException;

    void removeAllAuthData();
}
