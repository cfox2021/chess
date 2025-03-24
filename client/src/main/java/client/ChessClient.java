package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import server.ServerFacade;
import service.LoginResult;

import java.util.Arrays;

public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private String username;
    private String authToken;
    private State state = State.SIGNEDOUT;
    private GameData[] games;
    private int currentGameNum;
    private GameData currentGame;
    private String currentColor;
    private boolean isGameStarted;
    private boolean createdNewGame;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        isGameStarted = false;
    }

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state.equals(State.SIGNEDOUT)) {
                return switch (cmd) {
                    case "login", "l" -> login(params);
                    case "register", "r" -> register(params);
                    case "quit", "q" -> quit();
                    default -> help();
                };
            }
            return switch (cmd) {
                case "logout" -> logout();
                case "quit", "q" -> quit();
                case "create", "c" -> createGame(params);
                case "list", "l" -> listGames();
                case "join", "j" -> joinGame(params);
                default -> help();
            };

        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws DataAccessException {
        try{
            if (params.length == 2) {
                username = params[0];
                String password = params[1];
                LoginResult result = server.login(username, password);
                state = State.SIGNEDIN;
                authToken = result.authToken();
                return String.format("You signed in as %s.", username);
            }
            throw new DataAccessException("Could Not login, please follow this format:\n\"l\", \"login\" <USERNAME> <PASSWORD>");
        }
        catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        try{
            if (params.length == 3) {
                username = params[0];
                String password = params[1];
                String email = params[2];
                LoginResult result = server.register(username, password, email);
                state = State.SIGNEDIN;
                authToken = result.authToken();
                return String.format("You registered and signed in as %s.", username);
            }
            throw new DataAccessException("Could Not register user. Please follow this format:\n\"r\", \"register\" <USERNAME> <PASSWORD> <EMAIL>");
        }
        catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String logout() {
        try {
            server.logout(authToken);
            state = State.SIGNEDOUT;
            return "You have successfully logged out.";
        }
        catch(DataAccessException ex){
            return "Logout was unsuccessful.";
        }
    }

    public String quit(){
        System.exit(0);
        return "";
    }

    public String watchGame(){
        return "";
    }

    public String createGame(String... params) {
        try {
            if (params.length == 1) {
                String gameName = params[0];
                int result = server.createGame(gameName, authToken);
                createdNewGame = true;
                return (gameName + " successfully created.");
            }
            throw new DataAccessException("Could Not create game. Please follow this format: Create a new game:\n\"c\", \"create\" <GAME NAME>");
        }
        catch(DataAccessException ex){
                return "Could Not create game.";
            }
    }

    public String listGames() {
        try {
            games = server.listGames(authToken);

            if (games.length == 0) {
                return "No games found.";
            }

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < games.length; i++) {
                result.append(i + 1).append(": ");
                result.append("Game Name: ").append(games[i].gameName()).append(",   ");
                result.append("White: ").append(games[i].whiteUsername()).append(",   ");
                result.append("Black: ").append(games[i].blackUsername()).append("\n");
            }
            createdNewGame = false;
            return result.toString();
        }
        catch(DataAccessException ex){
            return "Unable to print games list.";
        }
    }

    public String joinGame(String... params){
        try {
            if (games == null || createdNewGame) {
                games = server.listGames(authToken);
            }
            if (params.length == 2) {
                currentGameNum = games[(Integer.parseInt(params[0]) - 1)].gameID();
                currentGame = games[(Integer.parseInt(params[0]) - 1)];
                currentColor = params[1].toLowerCase();

                server.joinGame(currentGameNum, currentColor, authToken);
                isGameStarted = true;
                return ("Successfully joined game as " + currentColor + ".");
            }
            throw new DataAccessException("Could Not create game, please follow this format:\n\"j\", \"join\" <GAME ID> <COLOR>");
        }
        catch(DataAccessException ex){
            return "Unable to join game.";
        }
    }

    public void clear() throws DataAccessException {
        server.clear();
    }

    public boolean hasjoinedGame(){
        return isGameStarted;
    }

    public GameData getGameData(){
        return currentGame;
    }

    public String getColor(){
        return currentColor;
    }

    public String help() {
    if (state == State.SIGNEDOUT) {
        return """
                Options:
                - Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                - Register a new user. "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                - Quit the program: "q", "quit"
                - Print this message: "h", "help"
                
                """;
    }
    return """
            Options:
            - List current games: "l", "list"
            - Create a new game: "c", "create" <GAME NAME>
            - Join a game: "j", "join" <GAME ID> <COLOR>
            - Watch a game: "w", "watch" <GAME ID>
            - Logout: "logout"
            
            """;
    }

}
