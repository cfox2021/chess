package dataaccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthData(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {

    }

    @Override
    public void removeAuthData(String username) throws DataAccessException {

    }

    @Override
    public void removeAllAuthData() {

    }
}
