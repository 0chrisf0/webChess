package com.web.chess.controllers;
import com.web.chess.ChessBoardGUI;
import com.web.chess.models.APIRequest;
import com.web.chess.models.ConnectRequest;
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
		logger.info("FENFENFENFNENFEFN");
		ChessBoardGUI gui = gameService.getBoard(request.gameId);
		String inputFEN = request.data;
		// Call server-side functions for the effects and send back the modified state.
		try {
			gui.setupBoard(inputFEN);
		} catch (Exception e) {
			// TODO add websocket response?
			logger.info("ERROROEROERROEOR");
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
	public String click(@RequestBody APIRequest request) {
		ChessBoardGUI gui = gameService.getBoard(request.gameId);
		String pos = request.data;
		int row = Character.getNumericValue(pos.charAt(0));
		int col = Character.getNumericValue(pos.charAt(1));
		gui.buttonPress(row, col);
		messagingTemplate.convertAndSend("/topic/game/" + request.gameId, BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate) );
		return "SUCCESS";
	}

	@PostMapping("/create")
	public int create(@RequestBody Player player) {
		return gameService.createGame();
	}

	@PostMapping("/connect")
	public ResponseEntity<String> connect(@RequestParam String gameId) {
		try {
			gameService.connect(gameId);
		} catch (Exception exception) {
			return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("Succesful connection", HttpStatus.OK);
	}
}