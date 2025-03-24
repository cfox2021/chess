package dataaccess;

import chess.DataAccessException;
import model.UserData;


public class MemoryUserDAO implements UserDAO {

    private final DataBase db = DataBase.getInstance();

    public MemoryUserDAO() {
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        if (db.getUserData().containsKey(username)) {
            return db.getUserData().get(username);
        } else {
            throw new DataAccessException("User \"" + username + "\" not found");
        }
    }

    @Override
    public void addUserData(UserData userData) throws DataAccessException {

        if (!db.getUserData().containsKey(userData.username())) {
            db.getUserData().put(userData.username(), userData);
        } else {
            throw new DataAccessException("User \"" + userData.username() + "\" already exists");
        }
    }

    @Override
    public void removeAllUserData() {
        db.getUserData().clear();
    }
}
