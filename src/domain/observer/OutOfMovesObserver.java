package domain.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import common.Player;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.contracts.IActionRequest;
import domain.analyser.PossibleAtomicActionAnalyser;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.update.UpdateProcessor;
import domain.update.contracts.IObserver;

/**
 * This {@link IObserver} monitors the {@link IBoard} 
 * for possible out-of-moves occurrences every time the {@link Player}s switch turns.
 * If it found such an occurrence, it signals this to its own observers 
 * through the {@link IObserver#fireOutOfMoves(Player)} update.
 */
public class OutOfMovesObserver extends UpdateProcessor
{
	private static boolean isCurrentPlayerOutOfMoves(IReadOnlyBoard board, Player player)
	{
		Set<Location> locations = board.getPlayerPieces(player).keySet();
		for(Location location : locations)
		{
			if(canAct(board, player, location))
			{
				return false;
			}
		}
		return true;
	}
	
	private static <T extends IActionRequest> List<T> filterValidActionRequests(IReadOnlyBoard board, Player player, List<T> requests)
	{
		List<T> filteredRequests = new ArrayList<T>();
		for(T step : requests)
		{
			try
			{
				IAction action = ActionFactory.create(step, board);
				if(action.isValidOn(board, player)) //no need to execute in case of atomic actions
				{
					filteredRequests.add(step);
				}
			}
			catch (LocationOutOfRangeException e)
			{
				assert false;
			}
		}
		
		return filteredRequests;
	}

	private static boolean canAct(IReadOnlyBoard board, Player player, Location location)
	{
		PossibleAtomicActionAnalyser analyser = new PossibleAtomicActionAnalyser(board);
		List<IActionRequest> possibleActions = analyser.find(player, location);
		List<IActionRequest> validActions = filterValidActionRequests(board, player, possibleActions);
		
		return !validActions.isEmpty();
	}
	
	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
	}
	
	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		if(isCurrentPlayerOutOfMoves(board, switchedIn))
		{
			emitOutOfMoves(switchedIn);
		}
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
	}

	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
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
		this.fireSwitchPlayer(board, starter);
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
