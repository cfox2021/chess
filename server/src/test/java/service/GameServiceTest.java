package service;

import chess.DataAccessException;
import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requestResult.CreateGameRequest;
import requestResult.JoinGameRequest;

public class GameServiceTest {

    private GameService gameService;
    private UserService userService;
    private MySqlAuthDAO authDAO;
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameService = new GameService();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();
        gameService.clear();
        userService = new UserService();
        userService.clear();
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
        Assertions.assertDoesNotThrow( () -> {
            gameService.createGame("123", request);
        });
    }

    @Test
    public void testListGames() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        CreateGameRequest request = new CreateGameRequest("theChessGame");
        CreateGameRequest request2 = new CreateGameRequest("anotherGame");
        gameService.createGame("123", request);
        gameService.createGame("123", request);
        Assertions.assertEquals(gameDAO.getAllGames(), gameService.listGames("123"));
    }

    @Test
    public void testListGamesNoGames() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        Assertions.assertEquals(gameDAO.getAllGames(), gameService.listGames("123"));
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        authDAO.addAuthData(new AuthData("squibler145", "456"));
        int gameID = gameService.createGame("123", new CreateGameRequest("theChessGame"));
        System.out.println(gameID);
        JoinGameRequest request1 = new JoinGameRequest("WHITE", gameID);
        JoinGameRequest request2 = new JoinGameRequest("BLACK", gameID);
        Assertions.assertDoesNotThrow(() -> {
            gameService.joinGame("123", request1);
            gameService.joinGame("456", request2);
        });
    }

    @Test
    public void testClear() throws DataAccessException {
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        gameService.createGame("123", new CreateGameRequest("theChessGame"));
        gameService.clear();
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        Assertions.assertTrue(gameService.listGames("123").isEmpty());
    }

    @Test
    public void testClearAlreadyEmpty() throws DataAccessException {
        gameService.clear();
        authDAO.addAuthData(new AuthData("chaddicus", "123"));
        Assertions.assertTrue(gameService.listGames("123").isEmpty());
    }
}
