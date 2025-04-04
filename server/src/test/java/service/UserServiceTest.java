package service;

import chess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.RegisterRequest;

public class UserServiceTest {

    private UserService userService;
    private LoginResult expected;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userService = new UserService();
        expected = new LoginResult(null, null);
        userService.clear();
    }

    @Test
    public void testRegisterUser() throws DataAccessException {
        expected = new LoginResult("tractor13", "123");
        LoginResult actual = userService.register(new RegisterRequest("tractor13", "password", "email123@gooseville.net"));
        Assertions.assertEquals(expected.username(), actual.username());
    }

    @Test
    public void testRegisterUserAlreadyExists() throws DataAccessException {
        userService.register(new RegisterRequest("tractor13", "password", "email123@gooseville.net"));

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(new RegisterRequest("tractor13", "password", "email123@gooseville.net"));
        });
    }

    @Test
    public void testLoginUser() throws DataAccessException {
        expected = new LoginResult("Sprocket441", "");
        userService.userDAO.addUserData(new UserData("Sprocket441", "cantGuessThis", "testingathing@sot.org"));
        LoginResult actual = userService.login(new LoginRequest("Sprocket441", "cantGuessThis"));
        Assertions.assertEquals(expected.username(), actual.username());
    }

    @Test
    public void testLoginUserIncorrectPassword() throws DataAccessException {
        userService.userDAO.addUserData(new UserData("Sprocket441", "cantGuessThis", "testingathing@sot.org"));
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(new LoginRequest("Sprocket441", "isThisThePassword?"));
        });
    }

    @Test
    public void testLoginUserAlreadyLoggedIn() throws DataAccessException {
        expected = new LoginResult("Sprocket441", "");
        userService.register(new RegisterRequest("Sprocket441", "cantGuessThis", "testingathing@sot.org"));
        LoginRequest loginRequest = new LoginRequest("Sprocket441", "cantGuessThis");
        LoginResult actual = userService.login(loginRequest);
        actual = userService.login(loginRequest);
        Assertions.assertEquals(expected.username(), actual.username());
    }

    @Test
    public void testLogoutUser() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("Tangerine", "1313", "thervius.gervine@yahoo.com");
        LoginResult loginResult = userService.register(registerRequest);
        userService.authDAO.getAuthData(loginResult.authToken());
        userService.logout(new LogoutRequest(loginResult.authToken()));
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(new LogoutRequest(loginResult.authToken()));
        });
    }

    @Test
    public void testLogoutUserNotLoggedIn() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(new LogoutRequest("mr.Sneaky"));
        });
    }

    @Test
    public void testClear() throws DataAccessException {
        userService.register(new RegisterRequest("steve", "steve", "steve@steve"));
        userService.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(new LoginRequest("steve", "steve"));
        });
    }



}
