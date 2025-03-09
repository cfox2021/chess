package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public boolean addPlayer(String color, int gameID, String authToken) {
        return false;
    }

    @Override
    public void updateGameData(GameData gameData) {

    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void removeAllGameData() {

    }
}
