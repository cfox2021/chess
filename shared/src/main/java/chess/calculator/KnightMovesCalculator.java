package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();

        for (int i = -2; i <= 2; i++) {
            ChessPosition upperPosition = myPosition;
            ChessPosition lowerPosition = myPosition;
            if (i < 0) {
                int upperRowOffset = 3 + i;
                upperPosition = new ChessPosition(myPosition.getRow() + upperRowOffset, myPosition.getColumn() + i);

                int lowerRowOffset = -3 - i;
                lowerPosition = new ChessPosition(myPosition.getRow() + lowerRowOffset, myPosition.getColumn() + i);

            }
            if (i > 0) {
                int upperRowOffset = 3 - i;
                upperPosition = new ChessPosition(myPosition.getRow() + upperRowOffset, myPosition.getColumn() + i);

                int lowerRowOffset = -3 + i;
                lowerPosition = new ChessPosition(myPosition.getRow() + lowerRowOffset, myPosition.getColumn() + i);
            }
            if (i != 0) {
                if (isValidSpace(upperPosition) && (spaceEmpty(board, upperPosition) || spaceOccupiedByOpponent(board, upperPosition, color))) {
                    validMoves.add(new ChessMove(myPosition, upperPosition, null));
                }
                if (isValidSpace(lowerPosition) && (spaceEmpty(board, lowerPosition) || spaceOccupiedByOpponent(board, lowerPosition, color))) {
                    validMoves.add(new ChessMove(myPosition, lowerPosition, null));
                }
            }

        }
        return validMoves;
    }

}
