package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private DataBase db = DataBase.getInstance();

    public MemoryAuthDAO() {
    }

    @Override
    public AuthData getAuthData(String token) throws DataAccessException {
        if(db.getAuthData().containsKey(token)) {
            return db.getAuthData().get(token);
        }
        else{
            throw new DataAccessException("AuthData not found");
        }
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        for (AuthData auth : db.getAuthData().values()) {
            if (auth.username().equals(authData.username())) {
                removeAuthData(auth.authToken());
            }
        }
        db.getAuthData().put(authData.authToken(), authData);
        db.getAuthUsers().add(authData.username());

    }

    @Override
    public void removeAuthData(String token) throws DataAccessException {
        if(db.getAuthData().containsKey(token)){
            db.getAuthUsers().remove(db.getAuthData().get(token).username());
            db.getAuthData().remove(token);

        }
        else{
            throw new DataAccessException("AuthData not found");
        }
    }

    @Override
    public void removeAllAuthData() {
        db.getAuthData().clear();
        db.getAuthUsers().clear();
    }

}
