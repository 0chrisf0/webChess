package com.web.chess;
import com.web.chess.BoardToJSON.ClickResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ChessApplication {
	// SERVER STATE
	ChessBoardGUI gui = new ChessBoardGUI();


	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}

	// For Debugging:
	// private final Logger logger = LoggerFactory.getLogger(ChessApplication.class);

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

	@GetMapping("api/click")
	public ClickResponse click(@RequestParam String pos) {
		int row = Character.getNumericValue(pos.charAt(0));
		int col = Character.getNumericValue(pos.charAt(1));
		gui.buttonPress(row, col);
		return BoardToJSON.clickToJSON(gui.chessBoardSquares,gui.currentGamestate);
	}


}