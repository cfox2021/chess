package server;

import com.google.gson.JsonObject;
import spark.*;

public class Server {

    private final UserHandler userHandler = new UserHandler();
    private final GameHandler gameHandler = new GameHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);
        Spark.delete("/db", (req, res) -> {
            userHandler.clear();
            gameHandler.clear();
            res.status(200);
            return new JsonObject();
        });
        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);


        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
