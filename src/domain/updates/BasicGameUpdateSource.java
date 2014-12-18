package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IBasicGameObserver;
import domain.updates.contracts.IBasicGameUpdateSource;
import domain.updates.contracts.IBasicUpdate;

/**
 * A convenient, basic implementation of {@link IBasicGameUpdateSource}.  
 */
public class BasicGameUpdateSource implements IBasicGameUpdateSource
{
	private final List<IBasicGameObserver> basicObservers = new ArrayList<IBasicGameObserver>();
	private boolean disabled = false;
	
	protected boolean isDisabled()
	{
		return disabled;
	}
	
	protected void enableUpdateObserverss()
	{
		this.disabled = false;
	}
	
	protected void disableUpdateObservers()
	{
		this.disabled = true;
	}
	
	protected List<IBasicGameObserver> getBasicObservers()
	{
		return basicObservers;
	}
	
	protected void sendToObservers(IBasicUpdate update)
	{
		if(!isDisabled())
		{
			for(IBasicGameObserver observer : getBasicObservers())
			{
				update.sendTo(observer);
			}
		}
	}
	
	protected void updateObserversBoard(IReadOnlyBoard board, Player performer)
	{
		sendToObservers(new IBasicUpdate()
		{
			@Override
			public void sendTo(IBasicGameObserver observer)
			{
				observer.updateBoard(board, performer);
			}
		});
	}

	@Override
	public void subscribe(IBasicGameObserver observer)
	{
		getBasicObservers().add(observer);
	}
	
	@Override
	public void unsubscribe(IBasicGameObserver observer)
	{
		getBasicObservers().remove(observer);
	}
}
