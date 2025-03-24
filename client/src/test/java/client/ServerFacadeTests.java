package client;

import chess.ChessGame;
import chess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import shared.LoginResult;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void before() throws DataAccessException {
        facade = new ServerFacade("http://localhost:8080");
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testRegister() throws DataAccessException {
        LoginResult actual = facade.register("user", "pass", "email");
        Assertions.assertTrue(actual.username().equals("user") && actual.authToken().length() > 10);
    }

    @Test
    public void testRegisterAlreadyExist() throws DataAccessException {
        LoginResult actual = facade.register("user", "pass", "email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.register("user", "pass", "email");
        });
    }

    @Test
    public void testLogin() throws DataAccessException {
        LoginResult registerResult = facade.register("user", "pass", "email");
        facade.logout(registerResult.authToken());
        LoginResult actual = facade.login("user", "pass");
        Assertions.assertTrue(actual.username().equals("user") && actual.authToken().length() > 10);
    }

    @Test
    public void testLoginIncorrectPassword() throws DataAccessException {
        LoginResult registerResult = facade.register("user", "pass", "email");
        facade.logout(registerResult.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.login("user", "p");
        });
    }

    @Test
    public void testLogout() throws DataAccessException {
        LoginResult registerResult = facade.register("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> {
            facade.logout(registerResult.authToken());
        });
    }

    @Test
    public void testLogoutIncorrectAuthToken() throws DataAccessException {
        facade.register("user", "pass", "email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.logout("1234567");
        });
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> {
            facade.createGame("test", loginResult.authToken());
        });
    }

    @Test
    public void testCreateGameIncorrectAuthToken() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.createGame("test", "loginResult.authToken()");
        });
    }

    @Test
    public void testListGames() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        int gameID = facade.createGame("test", loginResult.authToken());
        GameData[] games = facade.listGames(loginResult.authToken());
        Assertions.assertEquals(new GameData(gameID, null, null, "test", new ChessGame()),games[0]);
    }

    @Test
    public void testListGamesIncorrectAuthToken() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        facade.createGame("test", loginResult.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.listGames("loginResult.authToken()");
        });
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        int gameID = facade.createGame("test", loginResult.authToken());
        Assertions.assertDoesNotThrow(() -> {
            facade.joinGame(gameID, "WHITE", loginResult.authToken());
            facade.joinGame(gameID, "BLACK", loginResult.authToken());
        });
    }

    @Test
    public void testJoinGameColorAlreadyTaken() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        int gameID = facade.createGame("test", loginResult.authToken());
        facade.joinGame(gameID, "WHITE", loginResult.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.joinGame(gameID, "WHITE", loginResult.authToken());
        });
    }

    @Test
    public void testClear() throws DataAccessException {
        LoginResult loginResult = facade.register("user", "pass", "email");
        int gameID = facade.createGame("test", loginResult.authToken());
        facade.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.joinGame(gameID, "WHITE", loginResult.authToken());
        });
    }


}
