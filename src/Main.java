import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.GameAnalyzer;
import domain.GameController;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;

//TODO testing
//TODO documentation
public class Main {
	public static void main(String[] args) throws IOException {
		Path file = Paths.get("data", "input", "testCatchPriority.txt");
		Board board = BoardFactory.create(file);
		UserInterface ui = new UserInterface();
		Game game = new Game(board, ui);
		GameAnalyzer analyzer = new GameAnalyzer(game);
		GameController controller = new GameController(game, analyzer);
		controller.subscribe(new GraphicalVisualizer());
		controller.subscribe(new BoardSaver(Paths.get("data", "output")));
		controller.play();
	}
}
