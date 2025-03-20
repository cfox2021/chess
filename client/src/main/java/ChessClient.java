import dataaccess.DataAccessException;
import model.GameData;
import server.ServerFacade;
import service.LoginResult;

import java.util.Arrays;
import java.util.Collection;

public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private String username;
    private String authToken;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
            return switch (cmd) {
                case "login", "l" -> login(params);
                case "logout" -> logout();
                case "register", "r" -> register(params);
                case "quit", "q" -> quit();
                case "create", "c" -> createGame(params);
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
            throw new DataAccessException("Could Not login - Incorrect number of parameters.");
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
            throw new DataAccessException("Could Not register - Incorrect number of parameters.");
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
            return ex.getMessage();
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
                return (gameName + " successfully created. GameID: " + result);
            }
            throw new DataAccessException("Could Not create game - Incorrect number of parameters.");
        }
        catch(DataAccessException ex){
                return ex.getMessage();
            }
    }

    public String listGames() {
        try {
            GameData[] games = server.listGames(authToken);
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < games.length; i++) {
                result.append(i).append(": ");
                result.append("Game Name: ").append(games[i].gameName()).append(",   ");
                result.append("White: ").append(games[i].whiteUsername()).append(",   ");
                result.append("Black: ").append(games[i].blackUsername()).append("\n");
            }

            return result.toString();
        }
        catch(DataAccessException ex){
            return ex.getMessage();
        }
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
