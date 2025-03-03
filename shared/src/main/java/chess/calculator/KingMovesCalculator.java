package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition spaceToMoveTo;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                spaceToMoveTo = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                if (spaceToMoveTo != myPosition && isValidSpace(spaceToMoveTo) && (spaceEmpty(board, spaceToMoveTo)
                        || spaceOccupiedByOpponent(board, spaceToMoveTo, color))) {
                    validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                }
            }
        }
        return validMoves;
    }

    @Override
    public boolean isValidSpace(ChessPosition position) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        return true;
    }

    @Override
    public boolean spaceEmpty(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean spaceOccupiedByOpponent(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        if (board.getPiece(position) != null) {
            if (board.getPiece(position).getTeamColor() != myColor) {
                return true;
            }
        }
        return false;
    }
}
