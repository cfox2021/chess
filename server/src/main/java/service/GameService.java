package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {

    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public GameService() {}

    public int createGame(String authToken, String gameName) throws DataAccessException {
        authenticateUser(authToken);
        return gameDAO.createGame(gameName);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        authenticateUser(authToken);
        return gameDAO.getAllGames();
    }

    public void joinGame(String authToken, String color, int gameID, String playerName) throws DataAccessException {
        authenticateUser(authToken);
        gameDAO.addPlayer(color, gameID, playerName);
    }

    public void authenticateUser(String authToken) throws DataAccessException {
        authDAO.getAuthData(authToken);
    }


}
