import java.io.IOException;
import java.nio.file.Paths;

import ui.TextualVisualizer;
import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.LegalActionChecker;
import domain.GameController;
import domain.OutOfMovesObserver;
import domain.PromotionObserver;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;

//TODO testing
//TODO documentation
public class Main
{
	public static void main(String[] args) throws IOException
	{
		Board board = BoardFactory.create(Paths.get("data", "input", "testOutOfMoves.txt"));
		UserInterface ui = new UserInterface();
		PromotionObserver promotionChecker = new PromotionObserver();
		OutOfMovesObserver oomChecker = new OutOfMovesObserver();
		Game game = new Game(board, ui);
		LegalActionChecker analyzer = new LegalActionChecker(game);
		GameController controller = new GameController(game, analyzer);
		oomChecker.subscribe(controller);
		promotionChecker.subscribe(controller);
		controller.subscribe(oomChecker);
		controller.subscribe(promotionChecker);
		controller.subscribe(new GraphicalVisualizer());
		controller.subscribe(new TextualVisualizer());
		controller.subscribe(new BoardSaver(Paths.get("data", "output")));
		controller.play();
	}
}
