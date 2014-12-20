import java.io.IOException;
import java.nio.file.Paths;

import ui.TextualVisualizer;
import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.Game;
import domain.LegalActionChecker;
import domain.GameController;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;
import domain.input.InputProvider;
import domain.observers.OutOfMovesObserver;
import domain.observers.PromotionObserver;

//TODO testing
//TODO documentation
public class Main
{
	public static void main(String[] args) throws IOException
	{
		Board board = BoardFactory.create(Paths.get("data", "input", "defaultSomeDames.txt"));
		Game game = new Game(board);
		InputProvider inputProvider = new InputProvider(new UserInterface(), new LegalActionChecker(game));
		GameController controller = new GameController(game, inputProvider);
		PromotionObserver promotionChecker = new PromotionObserver();
		OutOfMovesObserver oomChecker = new OutOfMovesObserver();
		inputProvider.subscribe(controller);
		oomChecker.subscribe(controller);
		promotionChecker.subscribe(controller);
		controller.subscribe(oomChecker);
		controller.subscribe(promotionChecker);
		controller.subscribe(new GraphicalVisualizer());
		controller.subscribe(new TextualVisualizer());
		controller.subscribe(new BoardSaver(Paths.get("data", "output")));
		controller.play();
		inputProvider.close();
	}
}
