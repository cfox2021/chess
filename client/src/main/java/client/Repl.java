package client;

import chess.ChessBoard;
import model.GameData;
import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    String[] rowNums = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String[] colNums = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Welcome to the Chess Program\n" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !result.equals("q")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {

        if (client.hasjoinedGame()){
            GameData gameData = client.getGameData();
            ChessBoard board = gameData.game().getBoard();
            StringBuilder boardString = new StringBuilder();
            String color = client.getColor();

            if (color.equals("WHITE")){
                boardString.append(EscapeSequences.).append(EscapeSequences.SET_BG_COLOR_BLACK).append(EscapeSequences.SET_TEXT_COLOR_WHITE);
                for (int i = 0; i < colNums.length; i++) {
                    boardString.append(colNums[i])
                }
            }
            else{

            }
            System.out.println(boardString.toString());
        }
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
