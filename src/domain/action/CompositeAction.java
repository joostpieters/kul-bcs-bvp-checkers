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

/**
 * Represents a composite {@link IAction}.
 * Contrary to {@link AtomicAction}s, composite {@link IAction}s comprise multiple subactions.
 * These subactions can be atomic, but do not have to be.
 */
public class CompositeAction extends UpdatePropagator implements IAction
{
	private final List<IAction> subactions = new ArrayList<IAction>();
	
	public CompositeAction(IAction... actions)
	{
		for(IAction action : actions)
		{
			addSubAction(action);
		}
	}
	
	protected List<IAction> getSubActions()
	{
		return subactions;
	}
	
	protected void addSubAction(IAction subaction)
	{
		getSubActions().add(subaction);
		subaction.subscribeBasic(this);
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link CompositeAction}s are valid iff their subactions are valid when executed sequentially.
	 */
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{
		if(getSubActions().size() < 2)
		{
			fireWarning("CompositeAction contains too few subactions.");
			throw new IllegalStateException("CompositeAction contains too few subactions.");
		}
		IBoard testBoard = board.getDeepClone();
		for(IAction action : getSubActions())
		{
			if(!action.isValidOn(testBoard, currentPlayer))
			{
				fireWarning(String.format("Subaction %s of composite action %s is invalid.", action, this));
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
		
		for(IAction action : getSubActions())
		{
			action.executeOn(board, currentPlayer);
		}
		
	}
	
	@Override
	public String toString()
	{
		List<String> list = getSubActions().stream().map(e -> e.toString()).collect(Collectors.toList());
		String[] strings = list.toArray(new String[list.size()]);
		return "(" + String.join(" + ", strings) + ")";
	}

	@Override
	public Location getFrom()
	{
		return getSubActions().get(0).getFrom();
	}

	@Override
	public boolean isCatch()
	{
		return getSubActions().stream().anyMatch(a -> a.isCatch());
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
			List<IAction> subActions = getSubActions();
			List<IAction> subActionsOther = casted.getSubActions();
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
		for(IAction action : getSubActions())
		{
			result = 37 * result + action.hashCode();
		}
		return result;
	}
}
