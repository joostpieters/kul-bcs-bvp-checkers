package domain.action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.update.UpdatePropagator;

public class CompositeAction extends UpdatePropagator implements IAction
{
	private final List<IAction> actions = new ArrayList<IAction>();
	
	public CompositeAction(IAction... actions)
	{
		for(IAction action : actions)
		{
			addAction(action);
		}
	}
	
	protected List<IAction> getActions()
	{
		return actions;
	}
	
	protected void addAction(IAction action)
	{
		getActions().add(action);
		action.subscribeBasic(this);
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
			enableUpdateObservers();
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
		String[] strings = list.toArray(new String[list.size()]);
		return "(" + String.join(" + ", strings) + ")";
	}

	@Override
	public Location getFrom()
	{
		return getActions().get(0).getFrom();
	}

	@Override
	public boolean isCatch()
	{
		return getActions().stream().anyMatch(a -> a.isCatch());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(this == obj)
		{
			return true;
		}
		if(obj instanceof CompositeAction)
		{
			CompositeAction casted = (CompositeAction)obj;
			List<IAction> subActions = getActions();
			List<IAction> subActionsOther = casted.getActions();
			int nbActions = subActions.size();
			if(nbActions != subActionsOther.size())
			{
				return false;
			}
			for(int i=0; i < nbActions; i++)
			{
				if(!subActions.get(i).equals(subActionsOther.get(i)))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		for(IAction action : getActions())
		{
			result = 37 * result + action.hashCode();
		}
		return result;
	}
}
