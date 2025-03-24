package service;

import chess.DataAccessException;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import shared.CreateGameRequest;
import shared.JoinGameRequest;

import java.util.Collection;

public class GameService {

    MySqlGameDAO gameDAO = new MySqlGameDAO();
    MySqlAuthDAO authDAO = new MySqlAuthDAO();

    public GameService(){
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
        String username = authenticateUser(authToken).username();
        return gameDAO.addPlayer(joinRequest.playerColor(), joinRequest.gameID(), authToken, username);
    }

    public AuthData authenticateUser(String authToken) throws DataAccessException {
        return authDAO.getAuthData(authToken);
    }

    public void clear() throws DataAccessException {
        gameDAO.removeAllGameData();
        authDAO.removeAllAuthData();
    }
}
