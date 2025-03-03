package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator, BishopRookSupportCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition spaceToMoveTo = myPosition;

        //Checks Spaces above
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() + 1, spaceToMoveTo.getColumn());
            if (isValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces Below
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() - 1, spaceToMoveTo.getColumn());
            if (isValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces to Right
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow(), spaceToMoveTo.getColumn() + 1);
            if (isValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces to Left
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow(), spaceToMoveTo.getColumn() - 1);
            if (isValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        return validMoves;
    }



}
