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

    public boolean addPlayer(String color, int gameID, String authToken) {
        GameData oldGameData = db.getGameData().get(String.valueOf(gameID));
        GameData newGameData;
        System.out.println("oldGameData: " + oldGameData + "\n");
        String username = db.getAuthData().get(authToken).username();
        if(color != null && color.equals("WHITE") && oldGameData.whiteUsername() == null){
            newGameData = new GameData(oldGameData.gameID(), username, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
        }
        else if(color != null && color.equals("BLACK") && oldGameData.blackUsername() == null){
            newGameData = new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), username, oldGameData.gameName(), oldGameData.game());
        }
        else{
            return false;
        }
        updateGameData(newGameData);
        return true;
    }

    public void updateGameData(GameData gameData) {
        db.getGameData().put(String.valueOf(gameData.gameID()), gameData);
    }

    public Collection<GameData> getAllGames()  {
        return db.getGameData().values();
    }

    public void removeAllGameData() {
        db.getGameData().clear();
        db.getGameNames().clear();
        db.setGameNum(0);
    }


}
