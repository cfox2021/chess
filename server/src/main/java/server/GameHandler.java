package server;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.UserData;
import service.*;
import com.google.gson.Gson;
import spark.*;
import service.GameService;

public class GameHandler {

    GameService gameService;

    public GameHandler() {
        this.gameService = new GameService();
    }

    public void clear() throws DataAccessException {
        gameService.clear();
    }
}
