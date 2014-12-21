package domain.input;

import ui.LocalizationManager;
import common.Player;
import domain.LegalActionChecker;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
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
			ActionRequest request = analyzeAction();
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
	
	private ActionRequest analyzeAction()
	{
		String move = getMove();
		if(move.matches("\\d+\\s*-\\s*\\d+")) //step or fly
		{
			String parts[] = move.split("\\s*-\\s*");
			int fromIndex = Integer.parseInt(parts[0]);
			int toIndex = Integer.parseInt(parts[1]);
			return new MoveActionRequest(fromIndex, toIndex);
		}
		else if(move.matches("\\d+(\\s*x\\s*\\d+)+")) //(multi-)(fly-)catch
		{
			String[] parts = move.split("\\s*x\\s*");
			if(parts.length == 2)
			{
				int fromIndex = Integer.parseInt(parts[0]);
				int toIndex = Integer.parseInt(parts[1]);
				return new AtomicCatchActionRequest(fromIndex, toIndex);
			}
			else
			{
				int[] indices = new int[parts.length];
				for(int i=0; i < parts.length; i++)
				{
					indices[i] = Integer.parseInt(parts[i]);
				}
				return new CatchActionRequest(indices);
			}
		}
		throw new IllegalArgumentException(LocalizationManager.getString("invalidPatternException"));
	}
}