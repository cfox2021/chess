package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;

    boolean addPlayer(String color, int gameID, String authToken, String username) throws DataAccessException;

    Collection<GameData> getAllGames() throws DataAccessException;

    void removeAllGameData() throws DataAccessException;
}
