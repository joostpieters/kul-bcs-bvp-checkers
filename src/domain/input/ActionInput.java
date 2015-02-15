package domain.input;

import ui.LocalizationManager;
import common.Player;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.contracts.IActionRequest;
import domain.action.request.ActionRequestFactory;
import domain.analyser.LegalActionAnalyser;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;
import domain.location.LocationOutOfRangeException;
import domain.update.UpdatePropagator;

/**
 * A kind of {@link IInput} that represents an action of the player. 
 */
public class ActionInput extends UpdatePropagator implements IInput
{
	private final String move;
	private final IGame game;
	private final LegalActionAnalyser legalActionChecker;
	private final ActionRequestFactory actionRequestFactory;

	private String getMove()
	{
		return move;
	}
	
	private IGame getGame()
	{
		return game;
	}
	
	private LegalActionAnalyser getLegalActionChecker()
	{
		return legalActionChecker;
	}
	
	private ActionRequestFactory getActionRequestFactory()
	{
		return actionRequestFactory;
	}
	
	public ActionInput(String move, IGame game, LegalActionAnalyser analyzer)
	{
		this.move = move;
		this.game = game;
		this.legalActionChecker = analyzer;
		this.actionRequestFactory = new ActionRequestFactory(game.getBoard().getSize());
	}

	/**
	 * Processes the given action input by first validating it and - if allowed - executing it. 
	 */
	@Override
	public boolean process() throws LocationOutOfRangeException
	{
		IGame game = getGame();
		IBoard board = game.getBoard();
		Player currentPlayer = game.getCurrentPlayer();
		
		try
		{
			IActionRequest request = getActionRequestFactory().create(currentPlayer, getMove());
			if(!getLegalActionChecker().isActionLegal(request))
			{
				emitWarning(LocalizationManager.getString("warningIllegalAction"));
				return false;
			}
			IAction action = ActionFactory.create(request, board);
			action.subscribeBasic(this);
			if(action.isValidOn(board, currentPlayer))
			{
				action.executeOn(board, currentPlayer);
				return true;
			}
			else
			{
				emitWarning(LocalizationManager.getString("warningInvalidAction"));
				return false;
			}
		}
		catch(IllegalArgumentException ex)
		{
			emitWarning(ex.getMessage());
			return false;
		}
	}
}