import java.io.File;
import java.io.FileNotFoundException;

import domain.board.Board;
import domain.board.BoardFactory;


public class Main {
	public static void main(String[] args) {
		File file = new File("data\\input\\default.txt");
		try {
			Board board = BoardFactory.create(file);
			GameController controller = new GameController(board);
			controller.play();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
