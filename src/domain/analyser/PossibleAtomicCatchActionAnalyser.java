package domain.analyser;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.AtomicActionCatch;
import domain.action.request.CatchActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

/**
 * Finds all possible {@link AtomicActionCatch}es.
 */
public class PossibleAtomicCatchActionAnalyser implements IAnalyser<CatchActionRequest>
{
	private final IReadOnlyBoard board;
	
	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}

	public PossibleAtomicCatchActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}
	
	@Override
	public List<CatchActionRequest> find(Player player, Location pieceLocation)
	{
		if(!getBoard().isLocationOccupiedBy(player, pieceLocation))
		{
			throw new IllegalArgumentException("Given pieceLocation did not contain a piece: " + pieceLocation);
		}
		
		List<CatchActionRequest> requests = new ArrayList<CatchActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(pieceLocation.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Right, Direction.Right)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(pieceLocation.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Left, Direction.Left)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(pieceLocation.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Right, Direction.Right)); } catch(LocationOutOfRangeException outOfRange) {}
		try { targets.add(pieceLocation.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Left, Direction.Left)); } catch(LocationOutOfRangeException outOfRange) {}
		
		for(Location target : targets)
		{
			DiagonalLocationPair pair = new DiagonalLocationPair(pieceLocation, target);
			if(	getBoard().isLocationFree(target) && 
				getBoard().isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				requests.add(new CatchActionRequest(player, pieceLocation, target));
			}
		}
		return requests;
	}
}
