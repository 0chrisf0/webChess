package com.web.chess.models;

 // ClickResponse class representing the response structure
public class ClickResponse {
    public String[][] boardstate;
    public Boolean[][] highlights;
    public String gamestate;

    public ClickResponse () {
        boardstate = new String[][]{
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
                {"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"}};
        highlights = new Boolean[][]{{false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false}};
        gamestate = "INACTIVE";
    }
}
