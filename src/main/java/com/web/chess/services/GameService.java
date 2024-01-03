package com.web.chess.services;

import com.web.chess.ChessBoardGUI;

public class GameService {

    ChessBoardGUI game;
    
    public ChessBoardGUI createGame (   ) {game = new ChessBoardGUI(); return game;}

    public void connect () {}
}
