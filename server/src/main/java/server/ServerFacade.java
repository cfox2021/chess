package server;

import com.google.gson.reflect.TypeToken;
import dataaccess.DataAccessException;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Collection;

import com.google.gson.Gson;
import model.GameData;
import service.*;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult login(String username, String password) throws DataAccessException {
        var path = "/session";
        var request = new LoginRequest(username, password);
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public LoginResult register(String username, String password, String email) throws DataAccessException {
        var path = "/user";
        var request = new RegisterRequest(username, password, email);
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public void logout(String authToken) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public int createGame(String gameName, String authToken) throws DataAccessException {
        var path = "/game";
        var request = new CreateGameRequest(gameName);
        record CreateGameResult(int gameID) {}
        CreateGameResult result = this.makeRequest("POST", path, request, CreateGameResult.class, authToken);
        return result.gameID;
    }

    public GameData[] listGames(String authToken) throws DataAccessException {
        var path = "/game";
        record ListGamesResult(GameData[] games) {}
        ListGamesResult result = this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
        return result.games;
    }

    public void joinGame(int gameID, String color, String authToken) throws DataAccessException {
        var path = "/game";
        color = color.toUpperCase();
        var request = new JoinGameRequest(color, gameID);
        this.makeRequest("PUT", path, request, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataAccessException("Whoops");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new DataAccessException("Status is unsuccessful: " + status);
                }
            }

            throw new DataAccessException("Status was not unsuccessful: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
