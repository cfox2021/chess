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

}
