package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private DataBase db = DataBase.getInstance();

    public MemoryGameDAO() {
    }

    public int createGame(String gameName) throws DataAccessException {
        if(db.getGameNames().contains(gameName)) {
            throw new DataAccessException("Game already exists");
        }
        if(db.getGameNum() == 99){
            db.setGameNum(0);
        }
        db.setGameNum(db.getGameNum() + 1);
        db.getGameData().put(String.valueOf(db.getGameNum()), new GameData(db.getGameNum(), null, null, gameName, new ChessGame()));
        db.getGameNames().add(gameName);
        return db.getGameNum();

    }

    public GameData getGameData(String gameID) throws DataAccessException {
        if (db.getGameData().containsKey(gameID)) {
            return db.getGameData().get(gameID);
        }
        else{
            throw new DataAccessException("Invalid gameID");
        }
    }

    public void addPlayer(String color, int gameID, String username) throws DataAccessException {
        GameData oldGameData = db.getGameData().get(String.valueOf(gameID));
        GameData newGameData;
        if(color.equals("white") && oldGameData.whiteUsername() == null){
            newGameData = new GameData(oldGameData.gameID(), username, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
        }
        else if(color.equals("black") && oldGameData.blackUsername() == null){
            newGameData = new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), username, oldGameData.gameName(), oldGameData.game());
        }
        else{
            throw new DataAccessException(color + " player is already taken");
        }
        updateGameData(newGameData);
    }

    public void updateGameData(GameData gameData) {
        db.getGameData().put(String.valueOf(gameData.gameID()), gameData);
    }

    public Collection<GameData> getAllGames() throws DataAccessException {
        if(db.getGameData().isEmpty()){
            throw new DataAccessException("No Games Available");
        }
        return db.getGameData().values();
    }

    public void removeAllGameData() {
        db.getGameData().clear();
        db.getGameNames().clear();
        db.setGameNum(0);
    }


}
