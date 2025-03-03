package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition spaceToMoveTo = myPosition;

        //Checks Spaces above
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() + 1, spaceToMoveTo.getColumn());
            if (checkIsValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces Below
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() - 1, spaceToMoveTo.getColumn());
            if (checkIsValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces to Right
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow(), spaceToMoveTo.getColumn() + 1);
            if (checkIsValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces to Left
        while (true) {
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow(), spaceToMoveTo.getColumn() - 1);
            if (checkIsValidMove(board, myPosition, color, validMoves, spaceToMoveTo)) {
                break;
            }
        }

        return validMoves;
    }

    private boolean checkIsValidMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, List<ChessMove> validMoves, ChessPosition spaceToMoveTo) {
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
        if (board.getPiece(position) instanceof ChessPiece) {
            if (board.getPiece(position).getTeamColor() != myColor) {
                return true;
            }
        }
        return false;
    }
}
