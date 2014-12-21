package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IGameObserver;
import domain.updates.contracts.IGameUpdateSource;
import domain.updates.contracts.IUpdate;

/**
 * A convenient, basic implementation of {@link IGameUpdateSource}.
 * This class is fully backwards compatible with {@link BasicGameUpdateSource}.
 */
public abstract class GameUpdateSource extends BasicGameUpdateSource implements IGameUpdateSource
{
	private final List<IGameObserver> observers = new ArrayList<IGameObserver>();
	
	protected List<IGameObserver> getObservers()
	{
		return observers;
	}
	
	private void sendToObservers(IUpdate update)
	{
		if(!isDisabled())
		{
			for(IGameObserver observer : getObservers())
			{
				update.sendTo(observer);
			}
		}
	}
	
	// Basic updates
	@Override
	protected void updateObserversBoard(IReadOnlyBoard board, Player performer)
	{
		super.updateObserversBoard(board, performer); //update basic observers
		sendToObservers(observer -> observer.updateBoard(board, performer));
	}
	
	@Override
	protected void updateObserversSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		super.updateObserversSwitchPlayer(board, switchedIn); //update basic observers
		sendToObservers(observer -> observer.switchPlayer(board, switchedIn));
	}
	
	@Override
	protected void updateObserversExecuteAction(IAction action)
	{
		super.updateObserversExecuteAction(action); //update basic observers
		sendToObservers(observer -> observer.executeAction(action));
	}
	
	
	//Other updates
	protected void updateObserversGameOver(Player winner)
	{
		sendToObservers(observer -> observer.gameOver(winner));
	}
	
	protected void updateObserversPromotion(IReadOnlyBoard board, Location location)
	{
		sendToObservers(observer -> observer.promotion(board, location));
	}
	
	protected void updateObserversOutOfMoves(Player player)
	{
		sendToObservers(observer -> observer.outOfMoves(player));
	}
	
	protected void updateObserversProposeRemise(Player proposer)
	{
		sendToObservers(observer -> observer.proposeRemise(proposer));
	}
	
	protected void updateObserversAcceptRemise()
	{
		sendToObservers(observer -> observer.acceptRemise());
	}
	
	protected void updateObserversDeclineRemise()
	{
		sendToObservers(observer -> observer.declineRemise());
	}
	
	protected void updateObserversResign(Player resignee)
	{
		sendToObservers(observer -> observer.resign(resignee));
	}
	
	protected void updateObserversStart(IReadOnlyBoard board, Player starter)
	{
		sendToObservers(observer -> observer.start(board, starter));
	}
	
	protected void updateObserversWarning(String message)
	{
		sendToObservers(observer -> observer.warning(message));
	}
	
	protected void updateObserversError(String message, Exception ex)
	{
		sendToObservers(observer -> observer.error(message, ex));
	}
	
	@Override
	public void subscribe(IGameObserver observer)
	{
		getObservers().add(observer);
	}
	
	@Override
	public void unsubscribe(IGameObserver observer)
	{
		getObservers().remove(observer);
	}
}
