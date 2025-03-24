package dataaccess;

import chess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MySqlAuthDAOTest {

    private MySqlAuthDAO authDAO;

    @BeforeAll
    public static void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        authDAO.removeAllAuthData();
    }

    @Test
    public void testGetAuthData() throws DataAccessException {
        AuthData expected = new AuthData("username", "12345");
        authDAO.addAuthData(expected);
        Assertions.assertEquals(expected, authDAO.getAuthData("12345"));
    }

    @Test
    public void testGetAuthDataDoesNotExist() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuthData("12345");
        });
    }

    @Test
    public void testAddAuthData() throws DataAccessException {
        AuthData expected = new AuthData("username", "12345");
        authDAO.addAuthData(expected);
        Assertions.assertEquals(expected, authDAO.getAuthData("12345"));
    }

    @Test
    public void testAddAuthDataAlreadyExists() throws DataAccessException {
        AuthData expected = new AuthData("username", "12345");
        authDAO.addAuthData(expected);
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.addAuthData(expected);
        });
    }

    @Test
    public void testRemoveAuthData() throws DataAccessException {
        AuthData expected = new AuthData("username", "12345");
        authDAO.addAuthData(expected);
        authDAO.removeAuthData("12345");
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuthData("12345");
        });
    }

    @Test
    public void testRemoveAuthDataDoesNotExist() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.removeAuthData("12345");
        });
    }

    @Test
    public void testRemoveAllAuthData() throws DataAccessException {
        AuthData auth1 = new AuthData("username", "12345");
        AuthData auth2 = new AuthData("username2", "67890");
        authDAO.addAuthData(auth1);
        authDAO.addAuthData(auth2);
        authDAO.removeAllAuthData();
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuthData("12345");
            authDAO.getAuthData("67890");
        });
    }
}
