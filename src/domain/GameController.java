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
	private final LegalActionChecker legalActionChecker;
	
	private Game getGame()
	{
		return game;
	}
	
	private LegalActionChecker getLegalActionChecker()
	{
		return legalActionChecker;
	}
	
	public GameController(Game game, LegalActionChecker analyzer)
	{
		this.game = game;
		this.legalActionChecker = analyzer;
	}
	
	public void play()
	{
		Game game = getGame();
		LegalActionChecker analyzer = getLegalActionChecker();
		updateObserversStart(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
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
				updateObserversWarning("Try again.");
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
		updateObserversBoard(board.getDeepClone(), getGame().getCurrentPlayer());
	}
	
	@Override
	public void outOfMoves(Player player)
	{
		super.outOfMoves(player);
		Player winner = player.getOpponent();
		getGame().gameOver(winner);
		updateObserversGameOver(winner);
	}
	
	@Override
	protected void updateObserversResign(Player resignee)
	{
		super.updateObserversResign(resignee);
		Player winner = resignee.getOpponent();
		getGame().gameOver(winner);
		updateObserversGameOver(winner);
	}
	
	@Override
	protected void updateObserversAgreeRemise()
	{
		super.updateObserversAgreeRemise();
		getGame().remise();
		updateObserversGameOver(null);
	}
}
