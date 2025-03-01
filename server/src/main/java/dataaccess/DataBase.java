package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class DataBase {

    private static DataBase instance = new DataBase();

    public static Map<String, UserData> userData = new HashMap<>();
    public static Map<String, GameData> gameData = new HashMap<>();
    public static Map<String, AuthData> authData = new HashMap<>();
    public static int gameNum = 0;
    public static Collection<String> authUsers = new HashSet<>();
    public static Collection<String> gameNames = new HashSet<>();

    private DataBase() {
    }

    public static DataBase getInstance() {
        return instance;
    }

    public Map<String, UserData> getUserData() {
        return userData;
    }

    public Map<String, GameData> getGameData() {
        return gameData;
    }

    public Map<String, AuthData> getAuthData() {
        return authData;
    }

    public int getGameNum() {
        return gameNum;
    }

    public void setGameNum(int gameNumber) {
        gameNum = gameNumber;
    }

    public Collection<String> getAuthUsers() {
        return authUsers;
    }

    public Collection<String> getGameNames() {
        return gameNames;
    }
}
