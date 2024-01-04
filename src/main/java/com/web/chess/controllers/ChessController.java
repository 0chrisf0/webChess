package com.web.chess.controllers;
import com.web.chess.ChessBoardGUI;
import com.web.chess.models.ConnectRequest;
import com.web.chess.services.BoardToJSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.web.chess.models.Player;
import com.web.chess.services.GameService;

@RestController
@RequestMapping("/api")
public class ChessController {
	ChessBoardGUI gui = new ChessBoardGUI();

	private final SimpMessagingTemplate messagingTemplate;
	private final GameService gameService;

	public ChessController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.gameService = gameService;
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
	public int create(@RequestBody Player player) {
		return gameService.createGame();
	}

	@PostMapping("/connect")
	public ResponseEntity<String> connect(@RequestParam int gameId) {
		try {
			gameService.connect(gameId);
		} catch (Exception exception) {
			return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("Succesful connection", HttpStatus.OK);
	}
}