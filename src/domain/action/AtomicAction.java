package domain.action;

import common.LocationPair;

public abstract class AtomicAction extends Action {
	private final LocationPair pair;
	
	protected LocationPair getPair()
	{
		return pair;
	}
	
	public AtomicAction(LocationPair pair)
	{
		this.pair = pair;
	}
}
