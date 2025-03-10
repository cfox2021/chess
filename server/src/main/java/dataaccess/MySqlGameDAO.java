package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO, DAOSupport {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?)";
        ChessGame game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        return executeUpdate(statement, gameName, gameJson);
    }

    @Override
    public boolean addPlayer(String color, int gameID, String authToken) {
        return false;
    }

    @Override
    public void updateGameData(GameData gameData) {

    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void removeAllGameData() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              'gameName' varchar(256) NOT NULL,
              'game' JSON NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
