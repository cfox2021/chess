package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();
        int rowBeforePromotion;
        int direction;
        ChessPosition oneStepForward;
        ChessPosition twoStepsForward;
        ChessPosition diagonalRightStep;
        ChessPosition diagonalLeftStep;
        boolean hasMoved;

        // Sets variables based on Color
        if (color == ChessGame.TeamColor.WHITE) {
            rowBeforePromotion = 7;
            direction = 1;
            hasMoved = myPosition.getRow() != 2;
        } else {
            rowBeforePromotion = 2;
            direction = -1;
            hasMoved = myPosition.getRow() != 7;
        }

        oneStepForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        twoStepsForward = new ChessPosition(myPosition.getRow() + (direction * 2), myPosition.getColumn());
        diagonalRightStep = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + 1);
        diagonalLeftStep = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() - 1);


        //Calculates if pawn can move 1 and 2 steps forward
        if (isValidSpace(oneStepForward) && spaceEmpty(board, oneStepForward)) {
            checkPawnCanPromote(myPosition, validMoves, rowBeforePromotion, oneStepForward);
            if (!hasMoved) {
                if (isValidSpace(twoStepsForward) && spaceEmpty(board, twoStepsForward)) {
                    validMoves.add(new ChessMove(myPosition, twoStepsForward, null));
                }
            }
        }

        //Calculates if Pawn can capture in column to the right
        checkPawnCanCapture(board, myPosition, color, validMoves, rowBeforePromotion, diagonalRightStep);


        //Calculates if Pawn can capture in column to the left
        checkPawnCanCapture(board, myPosition, color, validMoves, rowBeforePromotion, diagonalLeftStep);


        System.out.println(validMoves);
        return validMoves;
    }

    private void checkPawnCanPromote(ChessPosition myPosition, List<ChessMove> validMoves, int rowBeforePromotion, ChessPosition oneStepForward) {
        if (myPosition.getRow() == rowBeforePromotion) {
            validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.KNIGHT));
            validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.QUEEN));
        } else {
            validMoves.add(new ChessMove(myPosition, oneStepForward, null));
        }
    }

    private void checkPawnCanCapture(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color,
                                     List<ChessMove> validMoves, int rowBeforePromotion, ChessPosition diagonalRightStep) {
        if (isValidSpace(diagonalRightStep) && spaceOccupiedByOpponent(board, diagonalRightStep, color)) {
            checkPawnCanPromote(myPosition, validMoves, rowBeforePromotion, diagonalRightStep);
        }
    }

}