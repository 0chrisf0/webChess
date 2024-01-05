package com.web.chess.services;

import com.web.chess.ChessBoardGUI;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GameService {
    HashMap<String, ChessBoardGUI> gameMap;
    int counter;

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    public GameService() {
        gameMap = new HashMap<String, ChessBoardGUI>();
        counter = 0;
    }

    public ChessBoardGUI getBoard(String gameId) {
        // TODO: Confirm this should never raise an error
        return gameMap.get(gameId);
    }
    
    public int createGame () {

        ChessBoardGUI game = new ChessBoardGUI();
        counter++;
        gameMap.put(Integer.toString(counter), game);
        return counter;
    }

    public void connect (String gameId) throws InvalidParameterException {
        ChessBoardGUI game = gameMap.get(gameId);
        if (game == null) {
            throw new InvalidParameterException("Game with " + gameId + " does not exist");
        }
        // rest of the logic
    }
}
