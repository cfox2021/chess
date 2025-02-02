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
        ChessBoard checkBoard;
        ChessPiece piece = getBoard().getPiece(startPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        if(getBoard().getPiece(startPosition) != null && !isInCheckmate(piece.getTeamColor())){
            if(!isInCheck(piece.getTeamColor())){
                for(ChessMove move : getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition)){
                    checkBoard = new ChessBoard(getBoard());
                    checkBoard.addPiece(move.getEndPosition(), piece);
                    checkBoard.addPiece(move.getStartPosition(), null);
                    if(!isBoardInCheck(checkBoard, piece.getTeamColor())){
                        moves.add(move);
                    }
                }
                return moves;
            }

            else {
                if(piece.getPieceType() == ChessPiece.PieceType.KING){
                    for(ChessMove move : getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition)){
                        checkBoard = new ChessBoard(getBoard());
                        checkBoard.addPiece(move.getEndPosition(), piece);
                        checkBoard.addPiece(move.getStartPosition(), null);
                        if(!isBoardInCheck(checkBoard, piece.getTeamColor())){
                            moves.add(move);
                        }
                    }
                    return moves;

                }
                else
                    return null;
            }
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece;
        if(getBoard().getPiece(move.getStartPosition()) != null){
            piece = getBoard().getPiece(move.getStartPosition());
            if (validMoves(move.getStartPosition()).contains(move) && piece.getTeamColor() == currentPlayer) {
                if(move.getPromotionPiece() != null){
                    piece.pieceType = move.getPromotionPiece();
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
        throw new RuntimeException("Not implemented");
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
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPiece checkPiece = board.getPiece(new ChessPosition(i, j));
                if(checkPiece != null && checkPiece.getTeamColor() != color){
                    for(ChessMove move : checkPiece.pieceMoves(board, checkPiece.getPosition())){
                        if (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(move.getEndPosition()).getTeamColor() == color){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
