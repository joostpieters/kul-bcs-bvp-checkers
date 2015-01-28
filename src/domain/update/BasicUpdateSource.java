package domain.update;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.update.contracts.IBasicObserver;
import domain.update.contracts.IBasicUpdate;
import domain.update.contracts.IBasicUpdateSource;

/**
 * A convenient, basic implementation of {@link IBasicUpdateSource}.  
 */
public abstract class BasicUpdateSource implements IBasicUpdateSource
{
	private final List<IBasicObserver> basicObservers = new ArrayList<IBasicObserver>();
	private boolean disabled = false;
	
	protected boolean isDisabled()
	{
		return disabled;
	}
	
	protected void enableUpdateObservers()
	{
		this.disabled = false;
	}
	
	protected void disableUpdateObservers()
	{
		this.disabled = true;
	}
	
	protected List<IBasicObserver> getBasicObservers()
	{
		return basicObservers;
	}
	
	protected void sendToBasicObservers(IBasicUpdate update)
	{
		if(!isDisabled())
		{
			for(IBasicObserver observer : getBasicObservers())
			{
				update.sendTo(observer);
			}
		}
	}
	
	protected void emitUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		sendToBasicObservers(observer -> observer.updateBoard(board, performer));
	}
	
	protected void emitSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		sendToBasicObservers(observer -> observer.switchPlayer(board, switchedIn));
	}

	protected void emitExecuteAction(IAction action)
	{
		sendToBasicObservers(observer -> observer.executeAction(action));
	}

	@Override
	public void subscribeBasic(IBasicObserver observer)
	{
		if(isSubscribedBasic(observer))
		{
			throw new IllegalStateException("Cannot subscribe twice");
		}
		getBasicObservers().add(observer);
	}
	
	@Override
	public void unsubscribeBasic(IBasicObserver observer)
	{
		if(!isSubscribedBasic(observer))
		{
			throw new IllegalStateException("Given observer was not subscribed");
		}
		getBasicObservers().remove(observer);
	}
	
	@Override
	public boolean isSubscribedBasic(IBasicObserver observer)
	{
		return getBasicObservers().contains(observer);
	}
}
