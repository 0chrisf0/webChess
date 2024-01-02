package com.web.chess.services;

import java.awt.Color;

import com.web.chess.ChessBoardGUI;
import com.web.chess.Piece;
import com.web.chess.models.ClickResponse;

public class BoardToJSON {

    public static String[][] boardToImagePaths (Piece[][] boardstate) {
        String[][] stringBoard = new String[8][8];
        for (int row = 0; row < 8;row++) {
            for(int col = 0; col < 8; col++) {
                stringBoard[row][col] = boardstate[row][col].getImagePath();
            }
        }
        return stringBoard;
    }

    public static ClickResponse clickToJSON (Piece[][] boardstate, ChessBoardGUI.gamestate state) {
        ClickResponse response = new ClickResponse();
        response.boardstate = (boardToImagePaths(boardstate));
        Boolean[][] highlights = new Boolean[8][8];
        for (int row = 0; row < 8;row++) {
            for(int col = 0; col < 8; col++) {
                highlights[row][col] = (Color.green == boardstate[row][col].getBackground());
            }
        }
        response.highlights = highlights;
        response.gamestate = state.toString();
        return response;
    }

}

