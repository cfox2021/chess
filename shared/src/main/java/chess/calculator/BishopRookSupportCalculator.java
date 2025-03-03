package chess.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public interface BishopRookSupportCalculator extends PieceMovesCalculator{
    default boolean isValidMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color,
                                List<ChessMove> validMoves, ChessPosition spaceToMoveTo) {
        if (isValidSpace(spaceToMoveTo)) {
            if (spaceEmpty(board, spaceToMoveTo) || spaceOccupiedByOpponent(board, spaceToMoveTo, color)) {
                validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                if (spaceOccupiedByOpponent(board, spaceToMoveTo, color)) {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
