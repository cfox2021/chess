import server.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Options:
                    - Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                    - Register a new user. "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    - Exit the program: "q", "quit"
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
