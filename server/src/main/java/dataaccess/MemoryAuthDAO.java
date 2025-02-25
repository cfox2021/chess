package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, AuthData> authDataBase;

    public MemoryAuthDAO() {
        authDataBase = new HashMap<>();
    }

    public AuthData GetAuthData(String token) throws DataAccessException {
        if(authDataBase.containsKey(token)){
            return authDataBase.get(token);
        }
        else{
            throw new DataAccessException("AuthData already exists");
        }
    }

    public void AddAuthData(AuthData authData) throws DataAccessException {
        if(!authDataBase.containsKey(authData.authToken())) {
            authDataBase.put(authData.authToken(), authData);
        }
        else{
            throw new DataAccessException("AuthData not found");
        }
    }

    public void RemoveAuthData(String token) throws DataAccessException {
        if(authDataBase.containsKey(token)){
            authDataBase.remove(token);
        }
        else{
            throw new DataAccessException("AuthData not found");
        }
    }
}
