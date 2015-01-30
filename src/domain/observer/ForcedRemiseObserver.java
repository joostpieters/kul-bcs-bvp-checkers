package domain.observer;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.update.UpdateProcessor;
import domain.update.contracts.IObserver;

/**
 * A simple type of {@link IObserver} that forces remise after a given 
 * number of whole actions have passed without a catch or a promotion.
 * This event is signaled to its own observers using the {@link IObserver#fireForcedRemise()} update. 
 */
public class ForcedRemiseObserver extends UpdateProcessor
{
	private int actionCounter = 0;
	private boolean catchDuringTurn = false;
	private final int threshold;
	
	private boolean isCatchDuringTurn()
	{
		return catchDuringTurn;
	}

	private void setCatchDuringTurn(boolean catchDuringTurn)
	{
		this.catchDuringTurn = catchDuringTurn;
	}

	private void incrementMoveCounter()
	{
		actionCounter++;
	}
	
	private void resetMoveCounter()
	{
		actionCounter = 0;
	}
	
	private boolean isThresholdReached()
	{
		return actionCounter >= threshold;
	}
	
	/**
	 * Creates a new {@link ForcedRemiseObserver} with the given threshold.
	 * 
	 * @param 	threshold
	 * 			The maximum number of whole actions without catching 
	 * 			or promotions after which remise is forced.
	 */
	public ForcedRemiseObserver(int threshold)
	{
		this.threshold = threshold;
	}
	
	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		if(isCatchDuringTurn())
		{
			resetMoveCounter();
		}
		else
		{
			incrementMoveCounter();
			if(isThresholdReached())
			{
				emitForcedRemise();
			}
		}
		setCatchDuringTurn(false);
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
		if(action.isCatch())
		{
			setCatchDuringTurn(true);
		}
	}

	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
		resetMoveCounter();
	}

	@Override
	public void fireGameOver(Player winner)
	{
	}

	@Override
	public void fireOutOfMoves(Player player)
	{
	}

	@Override
	public void fireProposeRemise(Player proposer)
	{
	}

	@Override
	public void fireAcceptRemise()
	{
	}

	@Override
	public void fireDeclineRemise()
	{
	}

	@Override
	public void fireForcedRemise()
	{
	}

	@Override
	public void fireResign(Player resignee)
	{
	}

	@Override
	public void fireStart(IReadOnlyBoard board, Player starter)
	{
	}

	@Override
	public void fireWarning(String message)
	{
	}

	@Override
	public void fireError(String message, Exception ex)
	{
	}
}
