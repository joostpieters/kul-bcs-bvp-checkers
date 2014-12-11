import java.io.File;
import java.io.FileNotFoundException;

import ui.IVisualizer;
import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.GameController;
import domain.board.Board;
import domain.board.BoardFactory;


public class Main {
	public static void main(String[] args) {
		File file = new File("data\\input\\defaultSomeDames.txt");
		try {
			Board board = BoardFactory.create(file);
			IVisualizer visualizer = new GraphicalVisualizer();
			UserInterface ui = new UserInterface(visualizer);
			Game game = new Game(board, ui);
			GameController controller = new GameController(game);
			controller.play();
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found");
		}
	}
}
