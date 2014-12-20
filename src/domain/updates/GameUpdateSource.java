package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.Action;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IGameObserver;
import domain.updates.contracts.IGameUpdateSource;
import domain.updates.contracts.IUpdate;

/**
 * A convenient, basic implementation of {@link IGameUpdateSource}.  
 */
public abstract class GameUpdateSource extends BasicGameUpdateSource implements IGameUpdateSource
{
	private final List<IGameObserver> observers = new ArrayList<IGameObserver>();
	
	protected List<IGameObserver> getObservers()
	{
		return observers;
	}
	
	protected void sendToObservers(IUpdate update)
	{
		if(!isDisabled())
		{
			for(IGameObserver observer : getObservers())
			{
				update.sendTo(observer);
			}
		}
	}
	
	protected void updateObserversBoard(IReadOnlyBoard board, Player performer)
	{
		super.updateObserversBoard(board, performer); //update basic observers
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.updateBoard(board, performer);
			}
		});
	}
	
	protected void updateObserversGameOver(Player winner)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.gameOver(winner);
			}
		});
	}
	
	protected void updateObserversPromotion(IReadOnlyBoard board, Location location)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.promotion(board, location);
			}
		});
	}
	
	protected void updateObserversOutOfMoves(Player player)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.outOfMoves(player);
			}
		});
	}
	
	protected void updateObserversProposeRemise(Player proposer)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.proposeRemise(proposer);
			}
		});
	}
	
	protected void updateObserversAcceptRemise()
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.acceptRemise();
			}
		});
	}
	
	protected void updateObserversDeclineRemise()
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.declineRemise();
			}
		});
	}
	
	protected void updateObserversResign(Player resignee)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.resign(resignee);
			}
		});
	}
	
	protected void updateObserversStart(IReadOnlyBoard board, Player starter)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.start(board, starter);
			}
		});
	}
	
	protected void updateObserversSwitchPlayer(IReadOnlyBoard board, Player performer)
	{
		super.updateObserversSwitchPlayer(board, performer); //update basic observers
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.switchPlayer(board, performer);
			}
		});
	}
	
	protected void updateObserversExecuteAction(Action action)
	{
		super.updateObserversExecuteAction(action); //update basic observers
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.executeAction(action);
			}
		});
	}
	
	protected void updateObserversWarning(String message)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.warning(message);
			}
		});
	}
	
	protected void updateObserversError(String message, Exception ex)
	{
		sendToObservers(new IUpdate()
		{
			@Override
			public void sendTo(IGameObserver observer)
			{
				observer.error(message, ex);
			}
		});
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
