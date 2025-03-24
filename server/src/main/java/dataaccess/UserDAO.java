package dataaccess;

import chess.DataAccessException;
import model.UserData;

public interface UserDAO {
    UserData getUserData(String username) throws DataAccessException;

    void addUserData(UserData userData) throws DataAccessException;

    void removeAllUserData() throws DataAccessException;
}
