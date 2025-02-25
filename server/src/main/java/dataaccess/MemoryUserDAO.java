package dataaccess;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;


public class MemoryUserDAO implements UserDAO {

    private Map<String, UserData> userDataBase;

    public MemoryUserDAO() {
        userDataBase = new HashMap<>();
    }

    public UserData GetUserData(String username) throws DataAccessException {
        if(userDataBase.containsKey(username)){
            return userDataBase.get(username);
        }
        else{
            throw new DataAccessException("User \"" + username + "\" already exists");
        }
    }

    public void AddUserData(UserData userData) throws DataAccessException {

        if (!userDataBase.containsKey(userData.username())) {
            userDataBase.put(userData.username(), userData);
        }
        else{
            throw new DataAccessException("User \"" + userData.username() + "\" already exists");
        }
    }
}
