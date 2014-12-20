package domain.action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.GameUpdatePropagator;
import domain.updates.contracts.IBasicGameObserver;

/**
 *
 * Note: this class should ideally extend {@link GameUpdatePropagator}
 * but already extends Action. Since java does not support multiple inheritance, 
 * some code used to propagate events is duplicated within. 
 */
public class CompositeAction extends Action implements IBasicGameObserver
{
	private final List<Action> actions = new ArrayList<Action>();
	
	public CompositeAction(Action... actions)
	{
		for(Action action : actions)
		{
			addAction(action);
		}
	}
	
	public CompositeAction() { }
	
	protected List<Action> getActions()
	{
		return actions;
	}
	
	protected void addAction(Action action)
	{
		getActions().add(action);
		action.subscribe(this);
	}
	
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{
		if(getActions().size() == 0)
		{
			throw new IllegalStateException("CompositeAction is empty.");
		}
		IBoard testBoard = board.getDeepClone();
		for(Action action : getActions())
		{
			if(!action.isValidOn(testBoard, currentPlayer))
			{
				return false;
			}
			disableUpdateObservers();
			action.executeOn(testBoard, currentPlayer);
			enableUpdateObserverss();
		}
		return true;
	}
	
	@Override
	public void executeOn(IBoard board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		for(Action action : getActions())
		{
			action.executeOn(board, currentPlayer);
		}
		
	}
	
	@Override
	public String toString()
	{
		List<String> list = getActions().stream().map(e -> e.toString()).collect(Collectors.toList());
		String[] strings = (String[]) list.toArray(new String[list.size()]);
		return "(" + String.join(" + ", strings) + ")";
	}

	@Override
	protected Location getFrom()
	{
		return getActions().get(0).getFrom();
	}

	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer) //propagate updates from actions to own observers
	{
		updateObserversBoard(board, performer);		
	}

	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		updateObserversSwitchPlayer(board, switchedIn);
	}

	@Override
	public void executeAction(Action action)
	{
		updateObserversExecuteAction(action);
	}
}
