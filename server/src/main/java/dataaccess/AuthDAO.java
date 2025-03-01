package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData getAuthData(String userName) throws DataAccessException;
    public void addAuthData(AuthData authData) throws DataAccessException;
    public void removeAuthData(String username) throws DataAccessException;
    public void removeAllAuthData();
}
