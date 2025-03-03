package chess.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color);

    boolean isValidSpace(ChessPosition position);

    boolean spaceEmpty(ChessBoard board, ChessPosition position);

    boolean spaceOccupiedByOpponent(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor);
}
