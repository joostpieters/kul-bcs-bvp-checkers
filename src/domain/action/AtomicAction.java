package domain.action;

import domain.action.contracts.IAction;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.update.BasicUpdateSource;

public abstract class AtomicAction extends BasicUpdateSource implements IAction
{
	private final DiagonalLocationPair pair;
	
	protected DiagonalLocationPair getPair()
	{
		return pair;
	}
	
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
