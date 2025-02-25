package server;

import dataaccess.DataAccessException;
import model.UserData;
import service.LoginRequest;
import service.LoginResult;
import service.UserService;
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



}
