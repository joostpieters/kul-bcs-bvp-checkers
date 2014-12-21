import java.io.IOException;
import java.nio.file.Paths;

import common.Configs;
import controller.GameController;
import ui.TextualVisualizer;
import ui.UserInterface;
import ui.GraphicalVisualizer;
import domain.LegalActionChecker;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSaver;
import domain.game.Game;
import domain.input.InputProvider;
import domain.observers.ForcedRemiseObserver;
import domain.observers.OutOfMovesObserver;
import domain.observers.PromotionObserver;

//TODO testing
//TODO documentation
public class Main
{
	public static void main(String[] args) throws IOException
	{
		Board board = BoardFactory.create(Paths.get("data", "input", "testPromotion.txt"));
		Game game = new Game(board);
		
		//try-with-resource (since Java 7)
		try(InputProvider inputProvider = new InputProvider(new UserInterface(), new LegalActionChecker(game)))
		{
			GameController controller = new GameController(game, inputProvider);
			PromotionObserver promotionObserver = new PromotionObserver();
			OutOfMovesObserver oomObserver = new OutOfMovesObserver();
			ForcedRemiseObserver forcedRemiseObserver = new ForcedRemiseObserver(Configs.RemiseThreshold);
			
			inputProvider.subscribe(controller);
			promotionObserver.subscribe(controller);
			controller.subscribeBasic(promotionObserver);
			oomObserver.subscribe(controller);
			controller.subscribeBasic(oomObserver);
			controller.subscribeBothWays(forcedRemiseObserver);
			controller.subscribe(new GraphicalVisualizer());
			controller.subscribe(new TextualVisualizer());
			controller.subscribeBasic(new BoardSaver(Paths.get("data", "output")));
			
			controller.play();
		}
	}
}
