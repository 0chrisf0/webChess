package com.web.chess.controllers;
import com.web.chess.ChessBoardGUI;
import com.web.chess.models.ClickResponse;
import com.web.chess.models.ConnectRequest;
import com.web.chess.services.BoardToJSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.web.chess.models.Player;

@SpringBootApplication
@RestController
public class ChessController {
	// SERVER STATE
	ChessBoardGUI gui = new ChessBoardGUI();


	public static void main(String[] args) {
		SpringApplication.run(ChessController.class, args);
	}


	@GetMapping("/api/FEN")
	public String[][] fen(@RequestParam String inputFEN) {
		// Call server-side functions for the effects and send back the modified state.
		try {
			gui.setupBoard(inputFEN);
		} catch (Exception e) {
			return new String[][]{
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"},
					{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"}};
		}
		return BoardToJSON.boardToImagePaths(gui.chessBoardSquares);

	}

	@GetMapping("/api/start")
	public String start() {
		gui.startGame();
		return gui.currentGamestate.toString();
	}

	@GetMapping("/api/click")
	public ClickResponse click(@RequestParam String pos) {
		int row = Character.getNumericValue(pos.charAt(0));
		int col = Character.getNumericValue(pos.charAt(1));
		gui.buttonPress(row, col);
		return BoardToJSON.clickToJSON(gui.chessBoardSquares, gui.currentGamestate);
	}

	@PostMapping("/api/create")
	public void create(@RequestBody Player player) {

	}

	@PostMapping("/api/connect")
	public void connect(@RequestBody ConnectRequest request) {

	}
}