package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color){
        List<ChessMove> validMoves = new ArrayList<>();
        int rowBeforePromotion;
        int direction;
        ChessPosition oneStepForward;
        ChessPosition twoStepsForward;
        ChessPosition diagonalRightStep;
        ChessPosition diagonalLeftStep;
        boolean hasMoved;

        // Sets variables based on Color
        if (color == ChessGame.TeamColor.WHITE){
            rowBeforePromotion = 7;
            direction = 1;
            hasMoved = myPosition.getRow() != 2;
        }
        else{
            rowBeforePromotion = 2;
            direction = -1;
            hasMoved = myPosition.getRow() != 7;
        }

        oneStepForward = new ChessPosition(myPosition.getRow() + direction,myPosition.getColumn());
        twoStepsForward = new ChessPosition(myPosition.getRow() + (direction * 2),myPosition.getColumn());
        diagonalRightStep = new ChessPosition(myPosition.getRow() + direction ,myPosition.getColumn() + 1);
        diagonalLeftStep = new ChessPosition(myPosition.getRow() + direction,myPosition.getColumn() - 1);


        //Calculates if pawn can move 1 and 2 steps forward
        if (isValidSpace(oneStepForward) && spaceEmpty(board, oneStepForward)){
            if (myPosition.getRow() == rowBeforePromotion){
                validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, oneStepForward, ChessPiece.PieceType.QUEEN));
            }
            else{
                validMoves.add(new ChessMove(myPosition, oneStepForward, null));
            }
            if (!hasMoved){
                if(isValidSpace(twoStepsForward) && spaceEmpty(board, twoStepsForward)){
                    validMoves.add(new ChessMove(myPosition, twoStepsForward, null));
                }
            }
        }

        //Calculates if Pawn can capture in column to the right
        if(isValidSpace(diagonalRightStep) && spaceOccupiedByOpponent(board, diagonalRightStep, color)) {
            if (myPosition.getRow() == rowBeforePromotion){
                validMoves.add(new ChessMove(myPosition, diagonalRightStep, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, diagonalRightStep, ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, diagonalRightStep, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, diagonalRightStep, ChessPiece.PieceType.QUEEN));
            }
            else {
                validMoves.add(new ChessMove(myPosition, diagonalRightStep, null));
            }
        }



        //Calculates if Pawn can capture in column to the left
        if(isValidSpace(diagonalLeftStep) && spaceOccupiedByOpponent(board, diagonalLeftStep, color)) {
            if (myPosition.getRow() == rowBeforePromotion){
                validMoves.add(new ChessMove(myPosition, diagonalLeftStep, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, diagonalLeftStep, ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, diagonalLeftStep, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, diagonalLeftStep, ChessPiece.PieceType.QUEEN));
            }
            else {
                validMoves.add(new ChessMove(myPosition, diagonalLeftStep, null));
            }
        }


        System.out.println(validMoves);
        return validMoves;
    }

    @Override
    public boolean isValidSpace(ChessPosition position) {
        if(position.getRow()  < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8){
            return false;
        }
        return true;
    }

    @Override
    public boolean spaceEmpty(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null){
            return true;
        }
        return false;
    }

    @Override
    public boolean spaceOccupiedByOpponent(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        if (board.getPiece(position) instanceof ChessPiece){
            if (board.getPiece(position).getTeamColor() != myColor) {
                return true;
            }
        }
        return false;
    }
}