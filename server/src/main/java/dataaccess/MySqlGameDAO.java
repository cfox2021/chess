package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO, DAOSupport {

    public MySqlGameDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?)";
        ChessGame game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        return executeUpdate(statement, gameName, gameJson);
    }

    @Override
    public boolean addPlayer(String color, int gameID, String authToken) throws DataAccessException { //continue from here
        GameData oldGame = null;
        GameData newGame;
        String username = "";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, String.valueOf(gameID));
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        oldGame = readGame(rs);
                    }
                    if (oldGame == null) {
                        throw new DataAccessException("Game not found for ID: " + gameID);
                    }
                }
            }

            var statement2 = "SELECT username FROM authData WHERE authToken=?";
            try (var ps2 = conn.prepareStatement(statement2)) {
                ps2.setString(1, String.valueOf(authToken));
                try (var rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        username = rs2.getString("username");
                    }
                    if (color != null && color.equals("WHITE") && oldGame.whiteUsername() == null) {
                        newGame = new GameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
                    } else if (color != null && color.equals("BLACK") && oldGame.blackUsername() == null) {
                        newGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
                    } else {
                        return false;
                    }
                    updateGameData(newGame);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not retrieve gameData");
        }
    }

    @Override
    public void updateGameData(GameData newGame) throws DataAccessException {
        var statement = "UPDATE gameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        var gameJson = new Gson().toJson(newGame.game());
        executeUpdate(statement, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), gameJson, newGame.gameID());
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        Collection <GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could Not read Games");
        }
        return games;
    }

    @Override
    public void removeAllGameData() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Could Not Delete gameData");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` JSON NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private GameData readGame(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        var gameID = rs.getInt("id");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        return new GameData (gameID,whiteUsername,blackUsername,gameName,(gson.fromJson(json, ChessGame.class)));
    }

}

