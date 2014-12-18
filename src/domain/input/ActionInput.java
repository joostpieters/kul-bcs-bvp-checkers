package domain.input;

import common.Player;
import domain.Game;
import domain.LegalActionChecker;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.Board;
import domain.input.contracts.IInput;
import domain.updates.GameUpdatePropagator;

public class ActionInput extends GameUpdatePropagator implements IInput
{
	private final String move;
	private final Game game;
	private final LegalActionChecker legalActionChecker;

	private String getMove()
	{
		return move;
	}
	
	private Game getGame()
	{
		return game;
	}
	
	private LegalActionChecker getLegalActionChecker() {
		return legalActionChecker;
	}
	
	public ActionInput(String move, Game game, LegalActionChecker analyzer)
	{
		this.move = move;
		this.game = game;
		this.legalActionChecker = analyzer;
	}

	@Override
	public boolean process()
	{
		Game game = getGame();
		Board board = game.getBoard();
		Player currentPlayer = game.getCurrentPlayer();
		
		try
		{
			ActionRequest request = analyzeAction();
			if(!getLegalActionChecker().isActionLegal(request))
			{
				return false;
			}
			Action action = ActionFactory.create(request, board, currentPlayer);
			action.subscribe(this);
			if(action.isValidOn(board, currentPlayer))
			{
				action.executeOn(board, currentPlayer);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(IllegalArgumentException ex)
		{
			updateObserversWarning(ex.getMessage());
			return false;
		}
	}
	
	private ActionRequest analyzeAction()
	{
		//1-7: step
		//1-45: fly = multi-step
		//1x12: catch
		//1x12x23: multi-catch
		//1x23: fly-catch
		//1x23x34: multi-fly-catch
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
		throw new IllegalArgumentException("Invalid pattern");
	}
}