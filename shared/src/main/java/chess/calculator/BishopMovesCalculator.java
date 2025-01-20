package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color){
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPosition spaceToMoveTo = myPosition;

        //Checks Spaces Up-Right
        while(true){
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() + 1, spaceToMoveTo.getColumn() + 1);
            if(isValidSpace(spaceToMoveTo)){
                if(spaceEmpty(board, spaceToMoveTo) || spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                    validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                    if(spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            else{
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces Down-Right
        while(true){
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() - 1, spaceToMoveTo.getColumn() + 1);
            if(isValidSpace(spaceToMoveTo)){
                if(spaceEmpty(board, spaceToMoveTo) || spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                    validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                    if(spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            else{
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces Down-Left
        while(true){
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() - 1, spaceToMoveTo.getColumn() - 1);
            if(isValidSpace(spaceToMoveTo)){
                if(spaceEmpty(board, spaceToMoveTo) || spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                    validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                    if(spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            else{
                break;
            }
        }

        spaceToMoveTo = myPosition;
        //Checks Spaces Up-Left
        while(true){
            spaceToMoveTo = new ChessPosition(spaceToMoveTo.getRow() + 1, spaceToMoveTo.getColumn() - 1);
            if(isValidSpace(spaceToMoveTo)){
                if(spaceEmpty(board, spaceToMoveTo) || spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                    validMoves.add(new ChessMove(myPosition, spaceToMoveTo, null));
                    if(spaceOccupiedByOpponent(board, spaceToMoveTo, color)){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            else{
                break;
            }
        }

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
