package dataaccess;
import chess.ChessGame;
import chess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;


public class MySqlGameDAOTest {

    private MySqlGameDAO gameDAO;
    private MySqlAuthDAO authDAO;

    @BeforeAll
    public static void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new MySqlGameDAO();
        gameDAO.removeAllGameData();
        authDAO = new MySqlAuthDAO();
        authDAO.removeAllAuthData();
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        int gameID = gameDAO.createGame("test");
        Assertions.assertEquals(new GameData(gameID, null, null, "test", new ChessGame()),
                gameDAO.getAllGames().iterator().next());
    }

    @Test
    public void testCreateGameAlreadyExists() throws DataAccessException {
        gameDAO.createGame("test");
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.createGame("test");
        });
    }

    @Test
    public void testGetAllGames() throws DataAccessException {
        int gameID1 = gameDAO.createGame("test");
        int gameID2 = gameDAO.createGame("test2");
        Collection <GameData> expected  = new ArrayList<GameData>();
        expected.add(new GameData(gameID1, null, null, "test", new ChessGame()));
        expected.add(new GameData(gameID2, null, null, "test2", new ChessGame()));
        Collection <GameData> actual = gameDAO.getAllGames();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAllGamesEmpty() throws DataAccessException {
        Assertions.assertTrue(gameDAO.getAllGames().isEmpty());
    }

    @Test
    public void testAddPlayer() throws DataAccessException {
        int gameId = gameDAO.createGame("test");
        authDAO.addAuthData(new AuthData("test", "test"));
        authDAO.addAuthData(new AuthData("test2", "test2"));
        gameDAO.addPlayer("WHITE", gameId, "test", "test");
        gameDAO.addPlayer("BLACK", gameId, "test2", "test2");
        Assertions.assertEquals(new GameData(gameId, "test", "test2", "test",
                new ChessGame()),gameDAO.getAllGames().iterator().next());
    }

    @Test
    public void testAddPlayerAlreadyTaken() throws DataAccessException {
        int gameId = gameDAO.createGame("test");
        authDAO.addAuthData(new AuthData("test", "test"));
        authDAO.addAuthData(new AuthData("test2", "test2"));
        gameDAO.addPlayer("WHITE", gameId, "test", "test");
        Assertions.assertFalse(gameDAO.addPlayer("WHITE", gameId, "test2", "test2"));
    }

    @Test
    public void testRemoveAllGameData() throws DataAccessException {
        gameDAO.createGame("test");
        gameDAO.createGame("test2");
        gameDAO.removeAllGameData();
        Assertions.assertTrue(gameDAO.getAllGames().isEmpty());
    }

}
