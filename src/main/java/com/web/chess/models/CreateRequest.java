package com.web.chess.models;

import com.web.chess.services.GameService;

public class CreateRequest {
    public int gameId;
    public ClickResponse state;

    public CreateRequest (Player player, GameService gameService) {
        gameId = gameService.createGame(player);
        state = new ClickResponse();
    }

}
