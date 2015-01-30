package domain.update;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.update.contracts.IBasicObserver;
import domain.update.contracts.IObserver;
import domain.update.contracts.IUpdate;
import domain.update.contracts.IUpdateSource;

/**
 * A convenient, basic implementation of {@link IUpdateSource}.
 * This class is fully backwards compatible with {@link BasicUpdateSource}.
 */
public abstract class UpdateSource extends BasicUpdateSource implements IUpdateSource
{
	private final List<IObserver> observers = new ArrayList<IObserver>();
	
	protected List<IObserver> getObservers()
	{
		return observers;
	}
	
	protected void sendToObservers(IUpdate update)
	{
		if(!isDisabled())
		{
			for(IObserver observer : getObservers())
			{
				update.sendTo(observer);
			}
		}
	}
	
	// Basic updates
	@Override
	protected void emitUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		super.emitUpdateBoard(board, performer); //update basic observers
		sendToObservers(observer -> observer.fireUpdateBoard(board, performer));
	}
	
	@Override
	protected void emitSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		super.emitSwitchPlayer(board, switchedIn); //update basic observers
		sendToObservers(observer -> observer.fireSwitchPlayer(board, switchedIn));
	}
	
	@Override
	protected void emitExecuteAction(IAction action)
	{
		super.emitExecuteAction(action); //update basic observers
		sendToObservers(observer -> observer.fireExecuteAction(action));
	}
	
	//Other updates
	protected void emitGameOver(Player winner)
	{
		sendToObservers(observer -> observer.fireGameOver(winner));
	}
	
	protected void emitPromotion(IReadOnlyBoard board, Location location)
	{
		sendToObservers(observer -> observer.firePromotion(board, location));
	}
	
	protected void emitOutOfMoves(Player player)
	{
		sendToObservers(observer -> observer.fireOutOfMoves(player));
	}
	
	protected void emitProposeRemise(Player proposer)
	{
		sendToObservers(observer -> observer.fireProposeRemise(proposer));
	}
	
	protected void emitAcceptRemise()
	{
		sendToObservers(observer -> observer.fireAcceptRemise());
	}
	
	protected void emitDeclineRemise()
	{
		sendToObservers(observer -> observer.fireDeclineRemise());
	}
	
	protected void emitResign(Player resignee)
	{
		sendToObservers(observer -> observer.fireResign(resignee));
	}
	
	protected void emitStart(IReadOnlyBoard board, Player starter)
	{
		sendToObservers(observer -> observer.fireStart(board, starter));
	}
	
	protected void emitForcedRemise()
	{
		sendToObservers(observer -> observer.fireForcedRemise());
	}
	
	protected void emitWarning(String message)
	{
		sendToObservers(observer -> observer.fireWarning(message));
	}
	
	protected void emitError(String message, Exception ex)
	{
		sendToObservers(observer -> observer.fireError(message, ex));
	}
	
	@Override
	public void subscribe(IObserver observer)
	{
		if(isSubscribed(observer))
		{
			//throw new IllegalStateException("Cannot subscribe twice");
			return;
		}
		getObservers().add(observer);
	}
	
	@Override
	public void unsubscribe(IObserver observer)
	{
		if(!isSubscribed(observer))
		{
			//throw new IllegalStateException("Given observer was not subscribed");
			return;
		}
		getObservers().remove(observer);
	}
	
	@Override
	public boolean isSubscribed(IBasicObserver observer)
	{
		return getObservers().contains(observer);
	}
}
