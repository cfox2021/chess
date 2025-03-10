package service;

import dataaccess.*;
import model.GameData;

import java.util.Collection;

public class GameService {

    MySqlGameDAO gameDAO = new MySqlGameDAO();
    MySqlAuthDAO authDAO = new MySqlAuthDAO();

    public GameService() throws DataAccessException {
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

    public void clear() throws DataAccessException {
        gameDAO.removeAllGameData();
        authDAO.removeAllAuthData();
    }
}
