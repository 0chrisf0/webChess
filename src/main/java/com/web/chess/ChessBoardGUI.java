package com.web.chess;
import java.awt.*;
import java.util.HashSet;



public class ChessBoardGUI {

    /**
     * chessBoardSquares is a 2D array representing all the squares of the chessboard.
     */
     public Piece[][] chessBoardSquares = new Piece[8][8];


    /**
     * The current gamestate, a value from enum gamestate.
     */
    public gamestate currentGamestate = gamestate.INACTIVE;

    public enum gamestate {
        INACTIVE,
        WHITE,
        WHITE_SELECT,
        BLACK,
        BLACK_SELECT,
        CHECKMATE,
        STALEMATE
    }

    /**
     * Represents the Board corresponding to the current GUI.
     */
    private Board board;

    /**
     * Represents the position of the last click on the chessBoard. Index 0 is the row and index 1
     * is the column.
     */
    private int[] lastclick = new int[2];

    /**
     * Represents the current set of legalMoves for the selected piece.
     */
    private HashSet<String> currentLegalMoves = new HashSet<>();


    /**
     * Constructor for ChessBoardGUI. Makes use of initializeGui().
     */
    public ChessBoardGUI() {
        initializeGui();
    }

    private void initializeGui() {
        // create the chess board squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessBoardSquares[row][col] = new Piece("Empty", false);
            }
        }
    }

    /**
     * Setup board with pieces given a starting position (FEN).
     * I.e: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
     */
    public Piece[][] setupBoard(String userInput) {
        if (currentGamestate != gamestate.INACTIVE) {
            currentGamestate = gamestate.INACTIVE;
            uncolor();
        }
        // Initialize the board with the given FEN
        String[] fields = userInput.split(" ");
        board = new Board(fields);
        String[] ranks = fields[0].split("/");
        // First Field: pieces and their positions "3R"
        for (int row = 0; row < 8; row++) {
            int column = 0;
            for (int j = 0; j < ranks[row].length(); j++ ) { //j = current index along rank entry
                try {
                    int empties = Integer.parseInt(ranks[row].substring(j,j+1));
                    for (int k = 0; k < empties; k++) {
                        Piece piece = new Piece("Empty", false);
                        chessBoardSquares[row][column].reinitialize(piece);
                        column++;
                    }
                } catch (NumberFormatException e) {
                    // Enforce hasMoved for pawns
                    Piece piece;
                    String pieceType = ranks[row].substring(j,j+1);
                    if (pieceType.equals("p") && row == 1) {
                        piece = new Piece(pieceType, false);
                    } else if (pieceType.equals("P") && row == 6) {
                        piece = new Piece(pieceType, false);
                    } else if (pieceType.equals("P") || pieceType.equals("p")) {
                        piece = new Piece(pieceType, true);
                    } else {
                        piece = new Piece(pieceType, false);
                    }
                    chessBoardSquares[row][column].reinitialize(piece);
                    column++;
                }
            } // I don't know if this is needed for valid FENs, but it is convenient
            if (column != 7) {
                while (column <= 7) {
                    Piece piece = new Piece("Empty", false);
                    chessBoardSquares[row][column].reinitialize(piece);
                    column++;
                }
            }
        }
        return chessBoardSquares;
    }

    /**
     * Starts the game, initializing the gamestate to the correct turn
     */
    public void startGame() {
        if (board == null) {
            return;
        }
        // Check if any checks or pins exist
        board.updateXray(chessBoardSquares);

        if (board.getTurn().equals("w")) {
            currentGamestate = gamestate.WHITE;
        } else {
            currentGamestate = gamestate.BLACK;
        }

    }

    /**
     * Handles the movement of the pieces on the GUI. Ensures that moves are legal.
     */
    public void buttonPress(int row, int column) {
        switch (currentGamestate) {
            case WHITE,BLACK:
                if (currentGamestate == gamestate.WHITE && chessBoardSquares[row][column].getColor() == -1) {
                    currentGamestate = gamestate.WHITE_SELECT;
                } else if (currentGamestate == gamestate.BLACK && chessBoardSquares[row][column].getColor() == 1) {
                    currentGamestate = gamestate.BLACK_SELECT;
                } else {
                    break;
                }
                currentLegalMoves = board.legalMoves(row, column, chessBoardSquares);
                // Color current piece and legalMoves
                chessBoardSquares[row][column].setBackground(Color.green);
                for (String position : currentLegalMoves) {
                    int[] coords = board.coordOfPosition(position);
                    chessBoardSquares[coords[0]][coords[1]].setBackground(Color.green);
                }
                break;
            case WHITE_SELECT, BLACK_SELECT:
                // Allow user to deselect the piece they chose
                if (row == lastclick[0] && column == lastclick[1]) {
                    if (currentGamestate == gamestate.WHITE_SELECT) {
                        currentGamestate = gamestate.WHITE;
                    } else {
                        currentGamestate = gamestate.BLACK;
                    }
                    uncolor();
                    break;
                }
                // These are the things that need to happen in this case statement (and BLACK_SELECT):
                // 1. Check if the move is legal
                // 2. IF the move is legal, make the move and update data accordingly (DETECT CHECKS)
                // otherwise set gamestate back to WHITE or BLACK.
                // 3. Check for promotion, then check for checks on the opposing King
                // 4. If there is a check, should automatically check for checkmate as well.
                if (currentLegalMoves.contains(board.positionOfCoord(row, column))) {
                    // Uncolor selection first so background can be used in makeMove()
                    uncolor();
                    // MAKE MOVE (make a helper function)
                    makeMove(row, column);
                    // Uncolor Selection and Check for Checks and update XRAY status
                    boardUpdate();
                    if (currentGamestate == gamestate.CHECKMATE) {
                        // END GAME
                    } else if (currentGamestate == gamestate.STALEMATE) {
                        // END GAME
                    }
                    // Switch turn
                    else if (currentGamestate == gamestate.WHITE_SELECT) {
                        currentGamestate = gamestate.BLACK;
                    } else {
                        currentGamestate = gamestate.WHITE;

                    }
                } else { // Illegal move selected, deselect piece
                    if (currentGamestate == gamestate.WHITE_SELECT) {
                        currentGamestate = gamestate.WHITE;
                    } else {
                        currentGamestate = gamestate.BLACK;
                    }
                    // UNCOLOR SELECTION
                    uncolor();
                }
                break;
            default:
                return;
        }
        lastclick[0] = row;
        lastclick[1] = column;
    }

    /**
     * Handles the execution of making a move on the board. Makes sure to update all necessary
     * fields. related to the backing data and the display on the GUI.
     */
    private void makeMove(int destRow, int destCol) {
        // Empty current passant moves
        board.emptyPassant();
        Piece empty = new Piece("Empty",false);
        Piece origin = chessBoardSquares[lastclick[0]][lastclick[1]];
        Piece target = chessBoardSquares[destRow][destCol];

        if(target.getColor() != 0) {
            // TODO: Do something regarding captures?
            //  E.g. listing the captured pieces on the gui somewhere?
        }
        if(origin.getType().equalsIgnoreCase("P")) {
            // Special things need to be done for pawns:
            // Promotion:
            if (currentGamestate == gamestate.WHITE_SELECT && destRow == 0) {
                origin.reinitialize(new Piece("Q", true));
            } else if (currentGamestate == gamestate.BLACK_SELECT && destRow == 7) {
                origin.reinitialize(new Piece("q", true));
            }
            // En Passant:
            // 1. Update things when pawns move 2 spaces.
            // 2. Capture the piece that should be captured
            if (Math.abs(destRow - lastclick[0]) > 1) {
                if(currentGamestate == gamestate.BLACK_SELECT) {
                    board.addPassant(board.positionOfCoord(destRow-1, destCol));
                } else {
                    board.addPassant(board.positionOfCoord(destRow+1, destCol));
                }
            }
            if (destCol != lastclick[1] && target.getType().equals("Empty")) {
                Piece passantTarget;
                if(currentGamestate == gamestate.BLACK_SELECT) {
                    passantTarget = chessBoardSquares[destRow-1][destCol];
                } else {
                    passantTarget = chessBoardSquares[destRow+1][destCol];
                }
                passantTarget.reinitialize(empty);
            }
            target.reinitialize(origin); // Is this all I need to do?
            target.setMoved(true);
            // This maintains the background because I am just setting the background
            // to what it already is
            origin.reinitialize(empty);
            return;
        }
        if(origin.getType().equalsIgnoreCase("K")) {
            // Special things to consider for kings:
            // Castling
            if (destCol - lastclick[1] > 1) {
                // Kingside castle
                Piece kingTarget = chessBoardSquares[destRow][destCol-1];
                Piece rookTarget = chessBoardSquares[lastclick[0]][lastclick[1]+1];
                rookTarget.reinitialize(target);
                rookTarget.setMoved(true);
                kingTarget.reinitialize(origin);
                kingTarget.setMoved(true);
                target.reinitialize(empty);
                origin.reinitialize(empty);
                if(rookTarget.getColor() == -1) {
                    board.removeCastle("K");
                } else {
                    board.removeCastle("k");
                }
                return;
            } else if (destCol - lastclick[1] < -1) {
                // Queenside castle
                Piece kingTarget = chessBoardSquares[destRow][destCol+2];
                Piece rookTarget = chessBoardSquares[lastclick[0]][lastclick[1]-1];
                rookTarget.reinitialize(target);
                rookTarget.setMoved(true);
                kingTarget.reinitialize(origin);
                kingTarget.setMoved(true);
                target.reinitialize(empty);
                origin.reinitialize(empty);
                // You can't castle twice
                if(rookTarget.getColor() == -1) {
                    board.removeCastle("Q");
                    board.removeCastle("K");
                } else {
                    board.removeCastle("q");
                    board.removeCastle("k");
                }
                return;
            }
            // Once the king moves he cannot castle
            if(origin.getColor() == -1) {
                board.removeCastle("Q");
                board.removeCastle("K");
            } else {
                board.removeCastle("q");
                board.removeCastle("k");
            }
            target.reinitialize(origin);
            target.setMoved(true);
            origin.reinitialize(empty);
            return;
        }
        // All other pieces
        target.reinitialize(origin); // Is this all I need to do?
        target.setMoved(true);
        origin.reinitialize(empty);

    }
    /**
     * Sets all the squares on the chessboard Piece's backgrounds to what they were originally.
     */
    public void uncolor() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoardSquares[i][j].originalBackground();
            }
        }
    }

    /**
     * Does the various checks that need to be made after each move is made, such as:
     * -Update each piece's Xray status
     * -Detect check and checkmate
     */
    public void boardUpdate() {
        // Refresh all pieces' pin status
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoardSquares[i][j].resetPin();
            }
        }
        // Detect check, checkmate, and draws
        if (currentGamestate == gamestate.WHITE_SELECT) {
            if (board.detectChecks(1, chessBoardSquares) > 0) {
                if (board.detectCheckmate(1, chessBoardSquares)) {
                    currentGamestate = gamestate.CHECKMATE;
                }
            } else {
                // Detect stalemate
                if (board.detectStalemate(1, chessBoardSquares)) {
                    currentGamestate = gamestate.STALEMATE;
                }
            }
        } else {
            if (board.detectChecks(-1, chessBoardSquares) > 0) {
                if (board.detectCheckmate(-1, chessBoardSquares)) {
                    currentGamestate = gamestate.CHECKMATE;
                }
            } else {
                // Detect stalemate
                if (board.detectStalemate(-1, chessBoardSquares)) {
                    currentGamestate = gamestate.STALEMATE;
                }
            }
        }

    }
}
