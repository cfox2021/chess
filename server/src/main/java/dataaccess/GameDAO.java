package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;

    boolean addPlayer(String color, int gameID, String authToken);

    void updateGameData(GameData gameData);

    Collection<GameData> getAllGames();

    void removeAllGameData();
}
