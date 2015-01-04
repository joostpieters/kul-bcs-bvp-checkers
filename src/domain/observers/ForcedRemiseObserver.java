package domain.observers;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.UpdateProcessor;
import domain.updates.contracts.IObserver;

/**
 * A simple type of {@link IObserver} that forces remise after a given 
 * number of whole actions have passed without a catch or a promotion.
 * This event is signaled to its own observers using the {@link IObserver#forcedRemise()} update. 
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
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
	}

	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
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
	public void executeAction(IAction action)
	{
		if(action.isCatch())
		{
			setCatchDuringTurn(true);
		}
	}

	@Override
	public void promotion(IReadOnlyBoard board, Location location)
	{
		resetMoveCounter();
	}

	@Override
	public void gameOver(Player winner)
	{
	}

	@Override
	public void outOfMoves(Player player)
	{
	}

	@Override
	public void proposeRemise(Player proposer)
	{
	}

	@Override
	public void acceptRemise()
	{
	}

	@Override
	public void declineRemise()
	{
	}

	@Override
	public void forcedRemise()
	{
	}

	@Override
	public void resign(Player resignee)
	{
	}

	@Override
	public void start(IReadOnlyBoard board, Player starter)
	{
	}

	@Override
	public void warning(String message)
	{
	}

	@Override
	public void error(String message, Exception ex)
	{
	}
}
