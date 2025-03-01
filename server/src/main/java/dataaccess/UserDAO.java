package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUserData(String username) throws DataAccessException;
    public void addUserData(UserData userData) throws DataAccessException;
    public void removeAllUserData();
}
