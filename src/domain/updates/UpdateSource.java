package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IBasicObserver;
import domain.updates.contracts.IObserver;
import domain.updates.contracts.IUpdateSource;
import domain.updates.contracts.IUpdate;

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
		sendToObservers(observer -> observer.updateBoard(board, performer));
	}
	
	@Override
	protected void emitSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		super.emitSwitchPlayer(board, switchedIn); //update basic observers
		sendToObservers(observer -> observer.switchPlayer(board, switchedIn));
	}
	
	@Override
	protected void emitExecuteAction(IAction action)
	{
		super.emitExecuteAction(action); //update basic observers
		sendToObservers(observer -> observer.executeAction(action));
	}
	
	//Other updates
	protected void emitGameOver(Player winner)
	{
		sendToObservers(observer -> observer.gameOver(winner));
	}
	
	protected void emitPromotion(IReadOnlyBoard board, Location location)
	{
		sendToObservers(observer -> observer.promotion(board, location));
	}
	
	protected void emitOutOfMoves(Player player)
	{
		sendToObservers(observer -> observer.outOfMoves(player));
	}
	
	protected void emitProposeRemise(Player proposer)
	{
		sendToObservers(observer -> observer.proposeRemise(proposer));
	}
	
	protected void emitAcceptRemise()
	{
		sendToObservers(observer -> observer.acceptRemise());
	}
	
	protected void emitDeclineRemise()
	{
		sendToObservers(observer -> observer.declineRemise());
	}
	
	protected void emitResign(Player resignee)
	{
		sendToObservers(observer -> observer.resign(resignee));
	}
	
	protected void emitStart(IReadOnlyBoard board, Player starter)
	{
		sendToObservers(observer -> observer.start(board, starter));
	}
	
	protected void emitForcedRemise()
	{
		sendToObservers(observer -> observer.forcedRemise());
	}
	
	protected void emitWarning(String message)
	{
		sendToObservers(observer -> observer.warning(message));
	}
	
	protected void emitError(String message, Exception ex)
	{
		sendToObservers(observer -> observer.error(message, ex));
	}
	
	@Override
	public void subscribe(IObserver observer)
	{
		if(isSubscribed(observer))
		{
			throw new IllegalStateException("Cannot subscribe twice");
		}
		getObservers().add(observer);
	}
	
	@Override
	public void unsubscribe(IObserver observer)
	{
		if(!isSubscribed(observer))
		{
			throw new IllegalStateException("Given observer was not subscribed");
		}
		getObservers().remove(observer);
	}
	
	@Override
	public boolean isSubscribed(IBasicObserver observer)
	{
		return getObservers().contains(observer);
	}
}
