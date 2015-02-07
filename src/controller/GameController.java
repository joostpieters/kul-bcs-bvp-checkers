package controller;

import ui.LocalizationManager;
import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;
import domain.input.contracts.IInputProvider;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.update.UpdatePropagator;

/**
 * The controller that controls the main {@link IGame} flow.
 */
public class GameController extends UpdatePropagator
{
	private final IGame game;
	private final IInputProvider inputProvider;
	
	private IGame getGame()
	{
		return game;
	}
	
	private IInputProvider getInputProvider()
	{
		return inputProvider;
	}
	
	public GameController(IGame game, IInputProvider inputProvider)
	{
		this.game = game;
		this.inputProvider = inputProvider;
	}
	
	/**
	 * Starts the {@link IGame}.
	 */
	public void play()
	{
		IGame game = getGame();
		emitStart(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
		
		while(!game.isOver())
		{
			IInput input = getInputProvider().askInput();
			boolean success;
			try
			{
				success = input.process();
			}
			catch(LocationOutOfRangeException e)
			{
				success = false;
			}
			
			if(success)
			{
				game.switchCurrentPlayer();
				emitSwitchPlayer(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
			}
			else
			{
				emitWarning(LocalizationManager.getString("failedInput"));
			}
		}
	}
	
	@Override
	public void firePromotion(IReadOnlyBoard readOnlyBoard, Location location) //augmented propagation
	{
		IBoard board = getGame().getBoard();
		
		if(!board.getReadOnlyBoard().equals(readOnlyBoard))
		{
			throw new IllegalStateException("promotion() called with wrong board.");
		}
		
		board.promotePiece(location);
		super.firePromotion(readOnlyBoard, location); //disseminate update
	}
	
	@Override
	public void fireOutOfMoves(Player player)
	{
		super.fireOutOfMoves(player);
		Player winner = player.getOpponent();
		getGame().gameOver(winner);
		emitGameOver(winner);
	}
	
	@Override
	protected void emitResign(Player resignee)
	{
		super.emitResign(resignee);
		Player winner = resignee.getOpponent();
		getGame().gameOver(winner);
		emitGameOver(winner);
	}
	
	@Override
	protected void emitAcceptRemise()
	{
		super.emitAcceptRemise();
		getGame().remise();
		emitGameOver(null);
	}
	
	@Override
	protected void emitForcedRemise()
	{
		super.emitForcedRemise();
		getGame().remise();
		emitGameOver(null);
	}
}
