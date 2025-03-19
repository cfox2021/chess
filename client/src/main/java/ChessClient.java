import dataaccess.DataAccessException;
import server.ServerFacade;

import java.util.Arrays;

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
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws DataAccessException {
        try{
            if (params.length == 2) {
                state = State.SIGNEDIN;
                username = params[0];
                String password = params[1];
                server.login(username, password);
                return String.format("You signed in as %s.", username);
            }
            throw new DataAccessException("Could Not login - Incorrect number of parameters.");
        }
        catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String logout(String... params) throws DataAccessException {
        try{
            if (params.length == 2) {
                state = State.SIGNEDIN;
                username = params[0];
                String password = params[1];
                server.login(username, password);
                return String.format("You signed in as %s.", username);
            }
            throw new DataAccessException("Could Not login - Incorrect number of parameters.");
        }
        catch (DataAccessException ex) {
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
