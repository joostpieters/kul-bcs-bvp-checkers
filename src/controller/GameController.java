package controller;


import ui.LocalizationManager;
import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.location.Location;
import domain.updates.UpdatePropagator;


public class GameController extends UpdatePropagator
{
	private final IGame game;
	private final InputProvider inputProvider;
	
	private IGame getGame()
	{
		return game;
	}
	
	private InputProvider getInputProvider()
	{
		return inputProvider;
	}
	
	public GameController(IGame game, InputProvider inputProvider)
	{
		this.game = game;
		this.inputProvider = inputProvider;
	}
	
	public void play()
	{
		IGame game = getGame();
		emitStart(game.getBoard().getReadOnlyBoard(), game.getCurrentPlayer());
		
		//main game loop
		while(!game.isOver())
		{
			IInput input = getInputProvider().askInput(game);
			boolean success = input.process();
			
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
