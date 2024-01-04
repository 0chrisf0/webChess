package com.web.chess.services;

import com.web.chess.ChessBoardGUI;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GameService {
    HashMap<String, ChessBoardGUI> gameMap;
    int counter;

    public GameService() {
        gameMap = new HashMap<String, ChessBoardGUI>();
        counter = 0;
    }
    
    public int createGame () {

        ChessBoardGUI game = new ChessBoardGUI();
        counter++;
        gameMap.put(Integer.toString(counter), game);

        return counter;
    }

    public void connect () {}
}
