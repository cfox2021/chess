package dataaccess;

import model.AuthData;


public class MemoryAuthDAO implements AuthDAO {

    private final DataBase db = DataBase.getInstance();

    public MemoryAuthDAO() {
    }

    @Override
    public AuthData getAuthData(String token) throws DataAccessException {
        if (db.getAuthData().containsKey(token)) {
            return db.getAuthData().get(token);
        } else {
            throw new DataAccessException("AuthData not found");
        }
    }

    @Override
    public void addAuthData(AuthData authData) {
        db.getAuthData().put(authData.authToken(), authData);
        db.getAuthUsers().add(authData.username());

    }

    @Override
    public void removeAuthData(String token) throws DataAccessException {
        if (db.getAuthData().containsKey(token)) {
            db.getAuthUsers().remove(db.getAuthData().get(token).username());
            db.getAuthData().remove(token);

        } else {
            throw new DataAccessException("AuthData not found");
        }
    }

    @Override
    public void removeAllAuthData() {
        db.getAuthData().clear();
        db.getAuthUsers().clear();
    }

}
