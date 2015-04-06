package domain.analyser;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.AtomicActionStep;
import domain.action.request.MoveActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Direction;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

/**
 * Finds all possible {@link AtomicActionStep}s.
 */
public class PossibleAtomicStepActionAnalyser implements IAnalyser<MoveActionRequest>
{
	private final IReadOnlyBoard board;

	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}

	public PossibleAtomicStepActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}

	public List<MoveActionRequest> find(Player player, Location location)
	{
		List<MoveActionRequest> requests = new ArrayList<MoveActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Right)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Left)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Right)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Left)); } catch(LocationOutOfRangeException outOfRange) {}
		for(Location target : targets)
		{
			if(	board.isLocationOccupiedBy(player, location) &&
				board.isLocationFree(target))
			{
				requests.add(new MoveActionRequest(player, location, target));
			}
		}
		return requests;
	}
}
