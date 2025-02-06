package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentPlayer;
    private ChessBoard gameBoard;

    public ChessGame() {
        setBoard(new ChessBoard());
        currentPlayer = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentPlayer;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentPlayer = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece playerPiece = getBoard().getPiece(startPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessMove> movesToCheck;

        if(playerPiece == null){
            return null;
        }

        movesToCheck = playerPiece.pieceMoves(getBoard(), startPosition);

        for(ChessMove move : movesToCheck){
            ChessPiece targetPiece = getBoard().getPiece(move.getEndPosition());
            getBoard().addPiece(move.getStartPosition(), null);
            getBoard().addPiece(move.getEndPosition(), playerPiece);

            if(!isBoardInCheck(getBoard(), playerPiece.getTeamColor())){
                moves.add(move);
            }
            getBoard().addPiece(move.getStartPosition(), playerPiece);
            getBoard().addPiece(move.getEndPosition(), targetPiece);

        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece;
        if(gameBoard != null){
            piece = getBoard().getPiece(move.getStartPosition());
        }
        else{
            throw new InvalidMoveException();
        }
        if(piece != null){
            if (validMoves(move.getStartPosition()).contains(move) && piece.getTeamColor() == currentPlayer) {
                if(move.getPromotionPiece() != null){
                    piece.setPieceType(move.getPromotionPiece());
                }
                getBoard().addPiece(move.getEndPosition(), piece);
                getBoard().addPiece(move.getStartPosition(), null);
                setTeamTurn(TeamColor.WHITE == piece.getTeamColor() ? TeamColor.BLACK : TeamColor.WHITE);
            }
            else{
                throw new InvalidMoveException();
            }
        }
        else{
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
            return isBoardInCheck(getBoard(), teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> kingPositions = new ArrayList<>();
        ChessBoard checkBoard;
        if (isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    if(getBoard().getPiece(new ChessPosition(i,j)) != null && getBoard().getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor && getBoard().getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING){
                        kingPositions = validMoves(new ChessPosition(i,j));
                    }
                }
            }
        }
        else{
            return false;
        }

        for (ChessMove move : kingPositions) {
            checkBoard = new ChessBoard(getBoard());
            checkBoard.addPiece(move.getEndPosition(), checkBoard.getPiece(move.getStartPosition()));
            checkBoard.addPiece(move.getStartPosition(), null);
            if(!isBoardInCheck(checkBoard, teamColor)){
                return false;
            }
        }
        return true;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            for(int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    ChessPiece checkPiece = getBoard().getPiece(new ChessPosition(i, j));
                    if(checkPiece != null && checkPiece.getTeamColor() == teamColor){
                        if(!validMoves(new ChessPosition(i, j)).isEmpty()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    private boolean isBoardInCheck(ChessBoard board, TeamColor color){
        if(board != null){
            for (int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    ChessPiece checkPiece = board.getPiece(new ChessPosition(i, j));
                    if(checkPiece != null && checkPiece.getTeamColor() != color){
                        Collection<ChessMove> movesToCheck = checkPiece.pieceMoves(board, new ChessPosition(i, j));
                        for(ChessMove move : movesToCheck){
                            ChessPiece targetPiece = board.getPiece(move.getEndPosition());
                            if (targetPiece != null && targetPiece.getPieceType() == ChessPiece.PieceType.KING && targetPiece.getTeamColor() == color){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
