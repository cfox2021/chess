package dataaccess;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class MySqlUserDAOTest {

    private MySqlUserDAO userDAO;

    @BeforeAll
    public static void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.removeAllUserData();
    }

    @Test
    public void testAddUser() throws DataAccessException {
        userDAO.addUserData(new UserData("username", "password", "email@email.com"));
        Assertions.assertEquals("username",userDAO.getUserData("username").username());
    }

    @Test
    public void testAddUserAlreadyExists() throws DataAccessException {
        userDAO.addUserData(new UserData("username", "password", "email@email.com"));
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUserData(new UserData("username", "password", "email@email.com"));
        });
    }

    @Test
    public void testRemoveUser() throws DataAccessException {
        userDAO.addUserData(new UserData("username", "password", "email@email.com"));
        userDAO.addUserData(new UserData("username2", "password1", "email@email3.com"));
        userDAO.removeAllUserData();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUserData("username");
        });
    }

    @Test
    public void testGetUserData() throws DataAccessException {
        UserData expected = new UserData("username", "password", "email@email.com");
        userDAO.addUserData(expected);
        UserData actual = userDAO.getUserData(expected.username());
        Assertions.assertTrue(actual.username().equals(expected.username()) && BCrypt.checkpw("password", actual.password())
                                && actual.email().equals(expected.email()));

    }

    @Test
    public void testGetUserDataDoesNotExist() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUserData("username");
        });
    }
}
