package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;

    boolean addPlayer(String color, int gameID, String authToken) throws DataAccessException;

    void updateGameData(GameData gameData) throws DataAccessException;

    Collection<GameData> getAllGames() throws DataAccessException;

    void removeAllGameData() throws DataAccessException;
}
