package service;

import chess.DataAccessException;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.RegisterRequest;

import java.util.UUID;

public class UserService {

    MySqlUserDAO userDAO;
    MySqlAuthDAO authDAO;

    public UserService(){
        userDAO = new MySqlUserDAO();
        authDAO = new MySqlAuthDAO();
    }


    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        LoginResult registerResult;
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        try {
            userDAO.getUserData(loginRequest.username());
            throw new DataAccessException("UserData already exists");
        } catch (Exception e) {
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.addUserData(userData);
            registerResult = login(loginRequest);
        }
        return registerResult;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        LoginResult loginResult = null;
        UserData userData = userDAO.getUserData(loginRequest.username());
        if (userData != null) {
            if (BCrypt.checkpw(loginRequest.password(), userData.password())) {
                String authToken = UUID.randomUUID().toString();
                AuthData authData = new AuthData(loginRequest.username(), authToken);
                authDAO.addAuthData(authData);
                loginResult = new LoginResult(userData.username(), authToken);
            } else {
                throw new DataAccessException("Password is Incorrect");
            }
        }
        return loginResult;
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        authDAO.removeAuthData(logoutRequest.authToken());
    }

    public void clear() throws DataAccessException {
        userDAO.removeAllUserData();
        authDAO.removeAllAuthData();
    }
}