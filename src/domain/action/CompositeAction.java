package domain.action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.GameUpdatePropagator;

public class CompositeAction extends GameUpdatePropagator implements IAction
{
	private final List<IAction> actions = new ArrayList<IAction>();
	
	public CompositeAction(IAction... actions)
	{
		for(IAction action : actions)
		{
			addAction(action);
		}
	}
	
	public CompositeAction() { }
	
	protected List<IAction> getActions()
	{
		return actions;
	}
	
	protected void addAction(IAction action)
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
		for(IAction action : getActions())
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
		
		for(IAction action : getActions())
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
	public Location getFrom()
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
	public void executeAction(IAction action)
	{
		updateObserversExecuteAction(action);
	}
}
