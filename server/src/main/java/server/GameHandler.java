package server;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import service.*;
import com.google.gson.Gson;
import spark.*;
import service.GameService;

import java.util.Collection;
import java.util.Map;

public class GameHandler {

    GameService gameService;

    public GameHandler() {
        this.gameService = new GameService();
    }

    public Object createGame(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        if(authToken == null || request == null) {
            res.status(400);
            return gson.toJson(Map.of("message", "Error: bad request"));
        }
        try{
            int gameID = gameService.createGame(authToken, request);
            res.status(200);
            return gson.toJson(Map.of("gameID", gameID));
        }
        catch(DataAccessException e){
            res.status(401);
            return gson.toJson(Map.of("message", "Error: unauthorized"));
        }


    }

    public Object listGames(Request req, Response res) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            Collection<GameData> games = gameService.listGames(authToken);
            res.status(200);
            return gson.toJson(games);
        }
        catch(DataAccessException e){
            res.status(401);
            return gson.toJson(Map.of("message", "Error: unauthorized"));
        }

    }

    public void clear() throws DataAccessException {
        gameService.clear();
    }
}
