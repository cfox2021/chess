package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DataBase;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {

    private GameService gameService;
    private DataBase db = DataBase.getInstance();
    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        db.getUserData().clear();
        db.getAuthData().clear();
        db.getAuthUsers().clear();
        db.getGameData().clear();
        db.getGameNames().clear();
    }

    @Test
    public void testAuthenticateUserHasAuthToken() throws DataAccessException {
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
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
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        int gameID = gameService.createGame("123", "theChessGame");
        Assertions.assertTrue(db.getGameData().containsKey(String.valueOf(gameID)));
    }

    @Test
    public void testCreateGameAlreadyExists() throws DataAccessException {
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        db.getGameData().put("1", new GameData(1, null, null, "theChessGame", new ChessGame()));
        db.getGameNames().add("theChessGame");
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame("123","theChessGame");
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
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames("123");
        });
    }

    @Test
    public void testJoinGameWhite() throws DataAccessException {
        GameData expected = new GameData(1, "chaddicus", "squibler145", "theChessGame", new ChessGame());
        db.getAuthData().put("123", new AuthData("chaddicus", "123"));
        db.getAuthData().put("456", new AuthData("squibler145", "456"));
        db.getGameData().put("1", new GameData(1, null, null, "theChessGame", new ChessGame()));
        gameService.joinGame("123", "white", 1, "chaddicus");
        gameService.joinGame("456", "black", 1, "squibler145");
        Assertions.assertEquals(expected, db.getGameData().get("1"));
    }
}
