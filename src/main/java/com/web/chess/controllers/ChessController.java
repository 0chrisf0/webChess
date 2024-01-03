package com.web.chess.controllers;
import com.web.chess.ChessBoardGUI;
import com.web.chess.models.ClickResponse;
import com.web.chess.models.ConnectRequest;
import com.web.chess.services.BoardToJSON;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.web.chess.models.Player;

@RestController
@RequestMapping("/api")
public class ChessController {
	ChessBoardGUI gui = new ChessBoardGUI();

	private final SimpMessagingTemplate messagingTemplate;

	public ChessController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}


	@GetMapping("/FEN")
	public String fen(String inputFEN) {
		// Call server-side functions for the effects and send back the modified state.
		try {
			gui.setupBoard(inputFEN);
		} catch (Exception e) {
			// TODO add websocket response?
		}
		messagingTemplate.convertAndSend("/topic/game", BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";

	}

	@GetMapping("/start")
	public String start() {
		gui.startGame();
		messagingTemplate.convertAndSend("/topic/game", BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";
	}

	@GetMapping("/click")
	public String click(@RequestParam String pos) {
		int row = Character.getNumericValue(pos.charAt(0));
		int col = Character.getNumericValue(pos.charAt(1));
		gui.buttonPress(row, col);
		messagingTemplate.convertAndSend("/topic/game", BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";
	}

	@PostMapping("/create")
	public void create(@RequestBody Player player) {

	}

	@PostMapping("/connect")
	public void connect(@RequestBody ConnectRequest request) {

	}
}