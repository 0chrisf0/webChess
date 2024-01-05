package com.web.chess.controllers;
import com.web.chess.ChessBoardGUI;
import com.web.chess.models.APIRequest;
import com.web.chess.models.ClickRequest;
import com.web.chess.models.ClickResponse;
import com.web.chess.models.ConnectRequest;
import com.web.chess.models.CreateRequest;
import com.web.chess.services.BoardToJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.web.chess.models.Player;
import com.web.chess.services.GameService;

@RestController
@RequestMapping("/api")
public class ChessController {


	private final SimpMessagingTemplate messagingTemplate;
	private final GameService gameService;

	private static final Logger logger = LoggerFactory.getLogger(ChessController.class);

	public ChessController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.gameService = gameService;
	}


	@PostMapping("/FEN")
	public String fen(@RequestBody APIRequest request) {
		ChessBoardGUI gui = gameService.getBoard(request.gameId);
		String inputFEN = request.data;
		// Call server-side functions for the effects and send back the modified state.
		try {
			gui.setupBoard(inputFEN);
		} catch (Exception e) {
			// TODO add websocket response?
		}
		messagingTemplate.convertAndSend("/topic/game/" + request.gameId, BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";

	}

	@PostMapping("/start")
	public String start(@RequestBody APIRequest request) {
		ChessBoardGUI gui = gameService.getBoard(request.gameId);
		gui.startGame();
		messagingTemplate.convertAndSend("/topic/game/" + request.gameId, BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";
	}

	@PostMapping("/click")
	public String click(@RequestBody ClickRequest request) {
		ChessBoardGUI gui = gameService.getBoard(request.gameId);
		String pos = request.position;
		int row = Character.getNumericValue(pos.charAt(0));
		int col = Character.getNumericValue(pos.charAt(1));
		if (request.color == 1) {
			row = -row + 7;
		}
		gui.buttonPress(row, col, request.color);
		messagingTemplate.convertAndSend("/topic/game/" + request.gameId, BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";
	}

	@PostMapping("/create")
	public CreateRequest create(@RequestBody Player player) {
		return new CreateRequest(player, gameService);
	}

	@PostMapping("/connect")
	public ResponseEntity<ClickResponse> connect(@RequestBody ConnectRequest request) {
		ChessBoardGUI gui;
		String gameId = request.gameId;
		Player player2 = request.player;
		try {
			gameService.connect(gameId, player2);
			gui = gameService.getBoard(request.gameId);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ClickResponse(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate), HttpStatus.OK);
	}
}