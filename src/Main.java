import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.GameController;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;


public class Main {
	public static void main(String[] args) throws IOException {
		Path file = Paths.get("data", "input", "defaultSomeDames.txt");
		Board board = BoardFactory.create(file);
		UserInterface ui = new UserInterface();
		Game game = new Game(board, ui);
		GameController controller = new GameController(game);
		controller.subscribe(new GraphicalVisualizer());
		controller.subscribe(new BoardSaver(Paths.get("data", "output")));
		controller.play();
	}
}
