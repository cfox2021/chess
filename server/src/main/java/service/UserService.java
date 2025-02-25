package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    MemoryUserDAO userDAO;
    MemoryAuthDAO authDAO;

    public UserService(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
    }


    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        LoginResult registerResult = null;
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        if (login(loginRequest) == null){
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.AddUserData(userData);
            registerResult = login(loginRequest);
        }
        return registerResult;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        LoginResult loginResult = null;
        UserData userData = userDAO.GetUserData(loginRequest.username());
        if(userData != null) {
            if(loginRequest.password().equals(userData.password())) {
                String authToken = UUID.randomUUID().toString();
                AuthData authData = new AuthData(loginRequest.username(), authToken);
                authDAO.AddAuthData(authData);
                loginResult = new LoginResult(userData.username(), authToken);
            }
        }
        return loginResult;
    }

    public void logout(LogoutRequest logoutRequest) {

    }
}