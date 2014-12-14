package domain;

import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.updates.GameUpdatePropagator;


public class GameController extends GameUpdatePropagator
{
	private final Game game;
	private final GameAnalyzer analyzer; //TODO put in Game?
	
	private Game getGame()
	{
		return game;
	}
	
	private GameAnalyzer getGameAnalyzer()
	{
		return analyzer;
	}
	
	public GameController(Game game, GameAnalyzer analyzer)
	{
		this.game = game;
		this.analyzer = analyzer;
		analyzer.subscribe(this);
	}
	
	public void play()
	{
		Game game = getGame();
		GameAnalyzer analyzer = getGameAnalyzer();
		updateFollowers(game.getBoard().getReadOnlyBoard());
		InputProvider inputProvider = new InputProvider();
		inputProvider.subscribe(this);
		while(!game.isOver())
		{
			
			if(analyzer.isCurrentPlayerOutOfMoves())
			{
				analyzer.processCurrentPlayerOutOfMoves();
				break;
			}
			IInput input = inputProvider.askInput(game, analyzer);
			boolean success = input.process();
			
			if(success)
			{
				analyzer.findAndPerformPromotions();
				game.switchCurrentPlayer();
			}
			else
			{
				game.getUI().showWarning("Could not process input, try again.");
			}
		}
		game.getUI().close();
	}
}
