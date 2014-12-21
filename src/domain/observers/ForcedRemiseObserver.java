package domain.observers;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.UpdateSource;
import domain.updates.contracts.IBasicUpdateProcessor;
import domain.updates.contracts.IObserver;
import domain.updates.contracts.IUpdateProcessor;

/**
 * A simple instance of {@link IObserver} that forces remise after
 * a given number of actions have passed without a catch or a promotion. 
 */
public class ForcedRemiseObserver extends UpdateSource implements IUpdateProcessor
{
	private int moveCounter = 0;
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
		moveCounter++;
	}
	
	private void resetMoveCounter()
	{
		moveCounter = 0;
	}
	
	private boolean isThresholdReached()
	{
		return moveCounter >= threshold;
	}
	
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
	
	@Override
	public void subscribeBasicBothWays(IBasicUpdateProcessor propagator)
	{
		this.subscribeBasic(propagator);
		propagator.subscribeBasic(this);
	}

	@Override
	public void subscribeBothWays(IUpdateProcessor processor)
	{
		this.subscribe(processor);
		processor.subscribe(this);
		
	}
}
