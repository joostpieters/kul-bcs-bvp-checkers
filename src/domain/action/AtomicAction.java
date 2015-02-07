package domain.action;

import domain.action.contracts.IAction;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.update.BasicUpdateSource;

/**
 * Represents an atomic {@link IAction}. Atomic actions are indivisible. They form the basic building blocks for more complex {@link IAction}s.
 */
public abstract class AtomicAction extends BasicUpdateSource implements IAction
{
	private final DiagonalLocationPair pair;
	
	protected DiagonalLocationPair getPair()
	{
		return pair;
	}
	
	/**
	 * Creates a new {@link AtomicAction} based on the given {@link DiagonalLocationPair}.
	 * 
	 * @param 	pair
	 * 			The from and to {@link Location}s of this pair will determine the start- and endpoints of this {@link AtomicAction}.
	 */
	public AtomicAction(DiagonalLocationPair pair)
	{
		this.pair = pair;
	}
	
	@Override
	public Location getFrom()
	{
		return getPair().getFrom();
	}
	
	@Override
	public abstract String toString();
	
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
		if(obj instanceof AtomicAction)
		{
			AtomicAction casted = (AtomicAction)obj;
			return 	isCatch() == casted.isCatch() &&
					getPair().equals(casted.getPair());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return 37 * getPair().hashCode() + Boolean.hashCode(isCatch());
	}
}
