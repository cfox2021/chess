package server;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.UserData;
import service.*;
import com.google.gson.Gson;
import spark.*;

import java.util.Map;

public class UserHandler {

    UserService userService;

    public UserHandler() {
        userService = new UserService();
    }

    public Object login(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        System.out.println("Received JSON: " + req.body());
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        System.out.println("loginRequest: " + loginRequest);
        LoginResult loginResult = userService.login(loginRequest);
        res.status(200);
        return gson.toJson(loginResult);
    }

    public Object register(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            res.status(400);
            return gson.toJson(Map.of("message", "Error: bad request"));

        }

        try{
            LoginResult registerResult = userService.register(registerRequest);
            res.status(200);
            return gson.toJson(registerResult);
        } catch (DataAccessException e) {
            res.status(403);
            return gson.toJson(Map.of("message", "Error: already taken"));
        }
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);
        res.status(200);
        return new JsonObject();
    }

    public void clear() throws DataAccessException {
        userService.clear();
    }



}
