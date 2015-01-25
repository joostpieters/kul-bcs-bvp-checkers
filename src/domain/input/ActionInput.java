package domain.input;

import ui.LocalizationManager;
import common.Player;
import domain.LegalActionChecker;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.request.ActionRequest;
import domain.action.request.ActionRequestFactory;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;
import domain.updates.UpdatePropagator;

public class ActionInput extends UpdatePropagator implements IInput
{
	private final String move;
	private final IGame game;
	private final LegalActionChecker legalActionChecker;

	private String getMove()
	{
		return move;
	}
	
	private IGame getGame()
	{
		return game;
	}
	
	private LegalActionChecker getLegalActionChecker()
	{
		return legalActionChecker;
	}
	
	public ActionInput(String move, IGame game, LegalActionChecker analyzer)
	{
		this.move = move;
		this.game = game;
		this.legalActionChecker = analyzer;
	}

	@Override
	public boolean process()
	{
		IGame game = getGame();
		IBoard board = game.getBoard();
		Player currentPlayer = game.getCurrentPlayer();
		
		try
		{
			ActionRequest request = ActionRequestFactory.create(getMove());
			if(!getLegalActionChecker().isActionLegal(request))
			{
				emitWarning(LocalizationManager.getString("warningIllegalAction"));
				return false;
			}
			IAction action = ActionFactory.create(request, board, currentPlayer);
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