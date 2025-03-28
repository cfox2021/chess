package dataaccess;

import chess.ChessGame;
import chess.DataAccessException;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGameDAO implements GameDAO, DAOSupport {

    public MySqlGameDAO(){
        try{
            configureDatabase(createStatements);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?)";
        ChessGame game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        return executeUpdate(statement, gameName, gameJson);
    }

    @Override
    public boolean addPlayer(String color, int gameID, String authToken, String username) throws DataAccessException {
        GameData oldGame = getGameData(gameID);
        GameData newGame;
        removeGameData(gameID);
        if (color != null && color.equals("WHITE") && oldGame.whiteUsername() == null) {
            newGame = new GameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else if (color != null && color.equals("BLACK") && oldGame.blackUsername() == null) {
            newGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        } else {
            return false;
        }
        updateGameData(newGame, color);
        return true;
    }


    private void updateGameData(GameData newGame, String color) throws DataAccessException {
        var statement = "";
        String whiteUsername = "";
        String blackUsername = "";

        ChessGame game = newGame.game();
        var gameJson = new Gson().toJson(game);

        if (color != null && color.equals("WHITE")) {
            whiteUsername = newGame.whiteUsername();
            if (newGame.blackUsername() != null) {
                statement = "INSERT INTO gameData (id, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
                blackUsername = newGame.blackUsername();
                executeUpdate(statement, String.valueOf(newGame.gameID()), whiteUsername, blackUsername,newGame.gameName(), gameJson);
            }
            else{
                statement = "INSERT INTO gameData (id, whiteUsername, gameName, game) VALUES (?, ?, ?, ?)";
                executeUpdate(statement, String.valueOf(newGame.gameID()), whiteUsername, newGame.gameName(), gameJson);

            }

        }
        else if (color != null && color.equals("BLACK")) {
            blackUsername = newGame.blackUsername();
            if (newGame.whiteUsername() != null) {
                statement = "INSERT INTO gameData (id, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
                whiteUsername = newGame.whiteUsername();
                executeUpdate(statement, String.valueOf(newGame.gameID()), whiteUsername, blackUsername, newGame.gameName(), gameJson);
            }
            else{
                statement = "INSERT INTO gameData (id, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
                executeUpdate(statement, String.valueOf(newGame.gameID()), blackUsername, newGame.gameName(), gameJson);
            }
        }

    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        Collection <GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
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
              `id` int NOT NULL AUTO_INCREMENT,
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
    private GameData getGameData(int gameID) throws DataAccessException {
        GameData game = null;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, String.valueOf(gameID));
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        game = readGame(rs);
                        return game;
                    }
                    throw new DataAccessException("Game not found for ID: " + gameID);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not retrieve gameData");
        }
    }

    public void removeGameData(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM gameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                int rowsRemoved = ps.executeUpdate();
                if (rowsRemoved == 0) {
                    throw new DataAccessException("Could not remove gameData");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Could Not Delete gameData");
        }
    }

}

