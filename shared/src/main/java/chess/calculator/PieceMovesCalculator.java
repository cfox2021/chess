package chess.calculator;

import chess.*;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color);

    default boolean isValidSpace(ChessPosition position){
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        return true;
    };

    default boolean spaceEmpty(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null) {
            return true;
        }
        return false;
    };

    default boolean spaceOccupiedByOpponent(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor){
        if (board.getPiece(position) instanceof ChessPiece) {
            if (board.getPiece(position).getTeamColor() != myColor) {
                return true;
            }
        }
        return false;
    }
}
