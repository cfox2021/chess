package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.EscapeSequences;

import java.util.Objects;
import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    String[] blackRowNums = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
    String[] whiteRowNums = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
    String[] whiteColNums = {"a", "b", "c", "d", "e", "f", "g", "h"};
    String[] blackColNums = {"h", "g", "f", "e", "d", "c", "b", "a"};

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

        if (client.hasjoinedGame()) {
            GameData gameData = client.getGameData();
            ChessBoard board = gameData.game().getBoard();
            StringBuilder boardString = new StringBuilder();
            String color = client.getColor().toUpperCase();

            String[] rowNums = color.equals("WHITE") ? whiteRowNums : blackRowNums;
            String[] colNums = color.equals("WHITE") ? whiteColNums : blackColNums;

            boardString.append("\n").append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY).append("  ")
                    .append(EscapeSequences.SET_TEXT_COLOR_BLACK);

            for (String colNum : colNums) {
                boardString.append("\u2003 ").append(colNum);
            }
            boardString.append(" ");
            boardString.append(EscapeSequences.RESET_BG_COLOR);
            boardString.append("\n");

            for (int rowIndex = 0; rowIndex < rowNums.length; rowIndex++) {
                String rowNum = rowNums[rowIndex];
                boardString.append(EscapeSequences.SET_TEXT_COLOR_BLACK)
                        .append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                        .append(rowNum);

                boolean isWhiteSpace = (rowIndex % 2 == 0);

                for (int colIndex = 0; colIndex < colNums.length; colIndex++) {
                    boardString.append(isWhiteSpace ? EscapeSequences.SET_BG_COLOR_WHITE
                            : EscapeSequences.SET_BG_COLOR_GREEN);

                    int boardRow = color.equals("WHITE") ? 8 - rowIndex : rowIndex + 1;
                    int boardCol = color.equals("WHITE") ? colIndex + 1 : 8 - colIndex;

                    ChessPiece piece = board.getPiece(new ChessPosition(boardRow, boardCol));

                    if (piece == null) {
                        boardString.append(EscapeSequences.EMPTY);
                    } else {
                        switch (piece.getPieceType()) {
                            case KING:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING);
                                break;
                            case QUEEN:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN);
                                break;
                            case BISHOP:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP);
                                break;
                            case KNIGHT:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT);
                                break;
                            case ROOK:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK);
                                break;
                            case PAWN:
                                boardString.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                                        ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN);
                                break;
                        }
                    }
                    
                    isWhiteSpace = !isWhiteSpace;
                }
                boardString.append(EscapeSequences.RESET_BG_COLOR).append("\n");
            }

            System.out.println(boardString);
        }
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
