package domain.action;

import common.RestrictedLocationPair;

public abstract class AtomicAction extends Action {
	private final RestrictedLocationPair pair;
	
	protected RestrictedLocationPair getPair()
	{
		return pair;
	}
	
	public AtomicAction(RestrictedLocationPair pair)
	{
		this.pair = pair;
	}
}
