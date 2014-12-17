package domain;

import common.Player;

import domain.board.contracts.IBoard;
import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.location.Location;
import domain.updates.GameUpdatePropagator;


public class GameController extends GameUpdatePropagator
{
	private final Game game;
	private final LegalActionChecker analyzer;
	
	private Game getGame()
	{
		return game;
	}
	
	private LegalActionChecker getGameAnalyzer()
	{
		return analyzer;
	}
	
	public GameController(Game game, LegalActionChecker analyzer)
	{
		this.game = game;
		this.analyzer = analyzer;
	}
	
	public void play()
	{
		Game game = getGame();
		LegalActionChecker analyzer = getGameAnalyzer();
		updateFollowers(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer()); //TODO update without player switch = bad? or special init event?
		InputProvider inputProvider = new InputProvider();
		inputProvider.subscribe(this);
		
		//main game loop
		while(!game.isOver())
		{
			IInput input = inputProvider.askInput(game, analyzer);
			boolean success = input.process();
			
			if(success)
			{
				game.switchCurrentPlayer();
			}
			else
			{
				game.getUI().showWarning("Could not process input, try again.");
			}
		}
		game.getUI().close();
	}
	
	@Override
	public void promotion(Location location)
	{
		IBoard board = getGame().getBoard();
		board.promotePiece(location);
		super.promotion(location); //disseminate update
		updateFollowers(board.getDeepClone(), getGame().getCurrentPlayer());
	}
	
	@Override
	public void outOfMoves(Player player)
	{
		Game game = getGame();
		Player winner = player.getOpponent();
		game.getUI().outOfMoves(player);
		game.gameOver(winner);
		super.outOfMoves(player);
		updateFollowersGameOver(winner);
	}
}
