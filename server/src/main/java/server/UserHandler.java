package server;

import com.google.gson.JsonObject;
import chess.DataAccessException;
import com.google.gson.Gson;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.RegisterRequest;
import service.UserService;
import spark.*;

import java.util.Map;

public class UserHandler {

    UserService userService;

    public UserHandler() {
        userService = new UserService();
    }

    public Object login(Request req, Response res) {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userService.login(loginRequest);
            res.status(200);
            return gson.toJson(loginResult);
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(Map.of("message", "Error: unauthorized"));
        }


    }

    public Object register(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            res.status(400);
            return gson.toJson(Map.of("message", "Error: bad request"));

        }

        try {
            LoginResult registerResult = userService.register(registerRequest);
            res.status(200);
            return gson.toJson(registerResult);
        } catch (DataAccessException e) {
            res.status(403);
            return gson.toJson(Map.of("message", "Error: already taken"));
        }
    }

    public Object logout(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            userService.logout(logoutRequest);
            res.status(200);
            return new JsonObject();
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(Map.of("message", "Error: unauthorized"));
        }
    }

    public void clear() throws DataAccessException {
        userService.clear();
    }


}
