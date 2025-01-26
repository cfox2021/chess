package chess;

import chess.calculator.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor teamColor;
    PieceType pieceType;
    ChessPosition position;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }
    public ChessPiece(ChessPiece pieceToCopy) {
        this.teamColor = pieceToCopy.teamColor;
        this.pieceType = pieceToCopy.pieceType;
        this.position = pieceToCopy.position;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator;

        if (this.pieceType == PieceType.PAWN) {
            calculator = new PawnMovesCalculator();
            return calculator.pieceMoves(board, myPosition, this.teamColor);
        }
        if (this.pieceType == PieceType.ROOK) {
            calculator = new RookMovesCalculator();
            return calculator.pieceMoves(board, myPosition, this.teamColor);
        }
        if (this.pieceType == PieceType.BISHOP) {
            calculator = new BishopMovesCalculator();
            return calculator.pieceMoves(board, myPosition, this.teamColor);
        }
        if (this.pieceType == PieceType.KING) {
            calculator = new KingMovesCalculator();
            return calculator.pieceMoves(board, myPosition, this.teamColor);
        }
        if (this.pieceType == PieceType.QUEEN) {
            calculator = new RookMovesCalculator();
            List<ChessMove> queenMoves = new ArrayList<>(calculator.pieceMoves(board, myPosition, this.teamColor));
            calculator = new BishopMovesCalculator();
            queenMoves.addAll(calculator.pieceMoves(board, myPosition, this.teamColor));
            return queenMoves;
        }
        if (this.pieceType == PieceType.KNIGHT) {
            calculator = new KnightMovesCalculator();
            return calculator.pieceMoves(board, myPosition, this.teamColor);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType, position);
    }
}
