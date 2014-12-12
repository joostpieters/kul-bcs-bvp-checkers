package domain.action;

import domain.location.DiagonalLocationPair;
import domain.location.Location;

public abstract class AtomicAction extends Action {
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
	protected Location getFrom() {
		return getPair().getFrom();
	}
}
