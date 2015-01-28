import java.io.IOException;
import java.nio.file.Paths;

import ui.GraphicalVisualizer;
import ui.TextualVisualizer;
import ui.UserInterface;
import common.Configs;
import controller.GameController;
import domain.action.LegalActionChecker;
import domain.board.BoardFactory;
import domain.board.contracts.IBoard;
import domain.game.Game;
import domain.input.InputProvider;
import domain.observer.ForcedRemiseObserver;
import domain.observer.OutOfMovesObserver;
import domain.observer.PromotionObserver;
import extensions.BoardSaver;

//TODO testing
//TODO documentation
public class Main
{
	public static void main(String[] args) throws IOException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "testPromotion.txt"));
		Game game = new Game(board);
		
		//try-with-resource (since Java 7)
		try(InputProvider inputProvider = new InputProvider(new UserInterface(), new LegalActionChecker(game), game))
		{
			GameController controller = new GameController(game, inputProvider);
			PromotionObserver promotionObserver = new PromotionObserver();
			OutOfMovesObserver oomObserver = new OutOfMovesObserver();
			ForcedRemiseObserver forcedRemiseObserver = new ForcedRemiseObserver(Configs.RemiseThreshold);
			
			inputProvider.subscribe(controller);
			promotionObserver.subscribe(controller);
			controller.subscribeBasic(promotionObserver);
			oomObserver.subscribe(controller);
			controller.subscribe(oomObserver);
			controller.link(forcedRemiseObserver);
			controller.subscribe(new GraphicalVisualizer());
			controller.subscribe(new TextualVisualizer());
			controller.subscribeBasic(new BoardSaver(Paths.get("data", "output")));
			
			controller.play();
		}
	}
}
