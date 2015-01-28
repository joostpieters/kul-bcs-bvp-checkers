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
}
