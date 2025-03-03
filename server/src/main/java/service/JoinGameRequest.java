package service;

public record JoinGameRequest(String playerColor , int gameID) {
    public JoinGameRequest(String playerColor) {
        this(playerColor, -1);
    }
    public JoinGameRequest(int gameID) {
        this(null, gameID);
    }
}
