package server;

import dataaccess.DataAccessException;
import model.UserData;
import service.*;
import com.google.gson.Gson;
import spark.*;

public class UserHandler {

    UserService userService;

    public UserHandler() {
        userService = new UserService();
    }

    public Object login(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        res.status(200);
        return gson.toJson(loginResult);
    }

    public Object register(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        System.out.println("Received JSON: " + req.body());
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        LoginResult registerResult = userService.register(registerRequest);
        res.status(200);
        return gson.toJson(registerResult);
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        LogoutRequest logoutRequest = gson.fromJson(req.body(), LogoutRequest.class);
        userService.logout(logoutRequest);
        res.status(200);
        return gson.toJson(null);
    }



}
