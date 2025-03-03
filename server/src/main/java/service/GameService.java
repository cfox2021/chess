package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {

    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public GameService() {
    }

    public int createGame(String authToken, CreateGameRequest createRequest) throws DataAccessException {
        authenticateUser(authToken);
        return gameDAO.createGame(createRequest.gameName());
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        authenticateUser(authToken);
        return gameDAO.getAllGames();
    }

    public boolean joinGame(String authToken, JoinGameRequest joinRequest) throws DataAccessException {
        authenticateUser(authToken);
        return gameDAO.addPlayer(joinRequest.playerColor(), joinRequest.gameID(), authToken);
    }

    public void authenticateUser(String authToken) throws DataAccessException {
        authDAO.getAuthData(authToken);
    }

    public void clear() {
        gameDAO.removeAllGameData();
        authDAO.removeAllAuthData();
    }
}
