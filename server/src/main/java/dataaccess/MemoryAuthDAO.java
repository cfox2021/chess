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
        return null;
    }

    public void AddAuthData(AuthData authData) throws DataAccessException {
        if(!authDataBase.containsKey(authData.authToken())) {
            authDataBase.put(authData.authToken(), authData);
        }
    }
}
