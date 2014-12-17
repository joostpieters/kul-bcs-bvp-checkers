import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.LegalActionChecker;
import domain.GameController;
import domain.OutOfMovesChecker;
import domain.PromotionChecker;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;

//TODO testing
//TODO documentation
public class Main {
	public static void main(String[] args) throws IOException {
		Path file = Paths.get("data", "input", "testOutOfMoves.txt");
		Board board = BoardFactory.create(file);
		UserInterface ui = new UserInterface();
		PromotionChecker promotionChecker = new PromotionChecker();
		OutOfMovesChecker oomChecker = new OutOfMovesChecker();
		Game game = new Game(board, ui);
		LegalActionChecker analyzer = new LegalActionChecker(game);
		GameController controller = new GameController(game, analyzer);
		oomChecker.subscribe(controller);
		promotionChecker.subscribe(controller);
		controller.subscribe(oomChecker);
		controller.subscribe(promotionChecker);
		controller.subscribe(new GraphicalVisualizer());
		controller.subscribe(new BoardSaver(Paths.get("data", "output")));
		controller.play();
	}
}
