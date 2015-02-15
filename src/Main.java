import java.io.IOException;
import java.nio.file.Paths;

import ui.GraphicalVisualizer;
import ui.TextualVisualizer;
import ui.UserInterface;
import common.ConfigurationManager;
import controller.GameController;
import domain.analyser.LegalActionAnalyser;
import domain.board.BoardFactory;
import domain.board.contracts.IBoard;
import domain.game.Game;
import domain.input.InputProvider;
import domain.location.LocationOutOfRangeException;
import domain.observer.ForcedRemiseObserver;
import domain.observer.OutOfMovesObserver;
import domain.observer.PromotionObserver;
import extensions.BoardSaver;

//TODO documentation
//TODO diagrams

/**
 * Contains the main entry point of this checkers game.
 */
public class Main
{
	/**
	 * The main entry point of this checkers game.
	 */
	public static void main(String[] args) throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "default.txt"));
		Game game = new Game(board);
		PromotionObserver promotionObserver = new PromotionObserver();
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		ForcedRemiseObserver forcedRemiseObserver = new ForcedRemiseObserver(ConfigurationManager.getInstance().getRemiseThreshold());
		
		try(UserInterface ui = new UserInterface())
		{
			try(InputProvider inputProvider = new InputProvider(ui, new LegalActionAnalyser(board), game))
			{
				GameController controller = new GameController(game, inputProvider);
				
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
}
