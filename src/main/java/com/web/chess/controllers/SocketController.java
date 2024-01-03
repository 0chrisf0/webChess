//package com.web.chess.controllers;
//
//import com.web.chess.models.ClickResponse;
//import com.web.chess.services.BoardToJSON;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.util.HtmlUtils;
//
//@Controller
//public class SocketController {
//
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public ClickResponse greeting(String pos) throws Exception {
//        int row = Character.getNumericValue(pos.charAt(0));
//        int col = Character.getNumericValue(pos.charAt(1));
//        gui.buttonPress(row, col);
//        return BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate);
//    }
//
//}