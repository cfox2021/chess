package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DataBase;
import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {

    private GameService gameService;
    private MySqlAuthDAO authDAO;
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameService = new GameService();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();
        gameService.clear();
    }

    @Test
    public void testAuthenticateUserHasAuthToken() throws DataAccessException {
        authDAO.addAuthData(new AuthData("SamwiseG", "123"));
        Assertions.assertDoesNotThrow(() -> {
            gameService.authenticateUser("123");
        });
    }

    @Test
    public void testAuthenticateUserHasNoAuthToken() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.authenticateUser("123");
        });
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        boolean gameFound = false;
        CreateGameRequest request = new CreateGameRequest("theChessGame");
        int gameID = gameService.createGame("123", request);
        for (GameData game : gameService.listGames("123")){
            if (game.gameID() == gameID){
                gameFound = true;
            }
        }
        Assertions.assertTrue(gameFound);
    }

    @Test
    public void testCreateGameAlreadyExists() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        CreateGameRequest request = new CreateGameRequest("theChessGame");
        gameService.createGame("123", request);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame("123", request);
        });
    }

    @Test
    public void testListGames() throws DataAccessException {
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        db.getGameData().put("1", new GameData(1, "someone", "someoneElse", "theChessGame", new ChessGame()));
        db.getGameData().put("2", new GameData(2, "null", "notNull", "anotherGame", new ChessGame()));
        Assertions.assertEquals(db.getGameData().values(), gameService.listGames("123"));
    }

    @Test
    public void testListGamesNoGames() throws DataAccessException {
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        Assertions.assertEquals(db.getGameData().values(), gameService.listGames("123"));
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        GameData expected = new GameData(1, "chaddicus", "squibler145", "theChessGame", new ChessGame());
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        db.getAuthData().put("456", new AuthData("squibler145", "456"));
        db.getGameData().put("1", new GameData(1, null, null, "theChessGame", new ChessGame()));
        db.getGameNames().add("theChessGame");
        JoinGameRequest request1 = new JoinGameRequest("WHITE", 1);
        JoinGameRequest request2 = new JoinGameRequest("BLACK", 1);
        gameService.joinGame("123", request1);
        gameService.joinGame("456", request2);
        Assertions.assertEquals(expected, db.getGameData().get("1"));
    }

    @Test
    public void testClear() throws DataAccessException {
        db.getAuthData().put("123", new AuthData("steve", "123"));
        db.getAuthUsers().add("steve");
        db.getGameData().put("1", new GameData(1, null, null, "theChessGame", new ChessGame()));
        db.getGameNames().add("theChessGame");
        gameService.clear();
        Assertions.assertTrue(db.getAuthData().isEmpty() && db.getAuthUsers().isEmpty() && db.getGameData().isEmpty() && db.getGameNames().isEmpty());
    }

    @Test
    public void testClearAlreadyEmpty() throws DataAccessException {
        gameService.clear();
        Assertions.assertTrue(db.getAuthData().isEmpty() && db.getAuthUsers().isEmpty() && db.getGameData().isEmpty() && db.getGameNames().isEmpty());
    }
}
