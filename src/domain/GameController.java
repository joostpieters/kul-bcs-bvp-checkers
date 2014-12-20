package domain;

import ui.LocalizationManager;
import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.location.Location;
import domain.updates.GameUpdatePropagator;


public class GameController extends GameUpdatePropagator
{
	private final Game game;
	private final InputProvider inputProvider;
	
	private Game getGame()
	{
		return game;
	}
	
	private InputProvider getInputProvider()
	{
		return inputProvider;
	}
	
	public GameController(Game game, InputProvider inputProvider)
	{
		this.game = game;
		this.inputProvider = inputProvider;
	}
	
	public void play()
	{
		Game game = getGame();
		updateObserversStart(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
		
		//main game loop
		while(!game.isOver())
		{
			IInput input = getInputProvider().askInput(game);
			boolean success = input.process();
			
			if(success)
			{
				game.switchCurrentPlayer();
				updateObserversSwitchPlayer(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
			}
			else
			{
				updateObserversWarning(LocalizationManager.getString("failedInput"));
			}
		}
	}
	
	@Override
	public void promotion(IReadOnlyBoard readOnlyBoard, Location location) //augmented propagation
	{
		IBoard board = getGame().getBoard();
		board.promotePiece(location);
		super.promotion(readOnlyBoard, location); //disseminate update
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
	protected void updateObserversAcceptRemise()
	{
		super.updateObserversAcceptRemise();
		getGame().remise();
		updateObserversGameOver(null);
	}
}
