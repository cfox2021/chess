package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class MemoryUserDAOTest {

    private MemoryUserDAO userDAO;
    private UserData expected;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        expected = new UserData(null, null, null);
    }

    @Test
    public void testAddNewUser() throws DataAccessException {
        UserData expected = new UserData("tractor13", "password", "notNull@gmail.com");
        userDAO.AddUserData(new UserData("tractor13", "password", "notNull@gmail.com"));
        Assertions.assertEquals(expected, userDAO.GetUserData("tractor13"));
    }

    @Test
    public void testAddUserAlreadyExists() throws DataAccessException {
        userDAO.AddUserData(new UserData("tractor13", "password", "notNull@gmail.com"));

        Assertions.assertThrows(DataAccessException.class, ()->{
            userDAO.AddUserData(new UserData("tractor13", "password", "notNull@gmail.com"));
        });
    }



}
