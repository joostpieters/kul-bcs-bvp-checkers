package domain.analyser;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.request.CatchActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Direction;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

/**
 * This {@link IAnalyser} find all possible flying catches on a given board.
 */
public class PossibleFlyCatchActionAnalyser implements IAnalyser<CatchActionRequest>
{
	private final IReadOnlyBoard board;

	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}
	
	public PossibleFlyCatchActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}
	
	@Override
	public List<CatchActionRequest> find(Player player, Location start)
	{
		List<CatchActionRequest> requests = new ArrayList<CatchActionRequest>();
		if(	!getBoard().isLocationOccupiedBy(player, start) ||
			!getBoard().getSquare(start).getPiece().canFly())
		{
			return requests;
		}
		
		for(Direction[] direction : Direction.getDiagonalDirections())
		{
			Location target = getPossibleSingleFlyCatchTargetLocationInDirection(player, start, direction);
			if(target != null)
			{
				List<Location> landingSpots = getPossibleSingleFlyCatchLandingLocationsInDirection(player, target, direction);
				for(Location landingSpot : landingSpots)
				{
					CatchActionRequest catchActionRequest = new CatchActionRequest(player, start, landingSpot);
					requests.add(catchActionRequest);
				}
			}
		}
		
		return requests;
	}
	
	private List<Location> getPossibleSingleFlyCatchLandingLocationsInDirection(Player player, Location target, Direction... direction)
	{
		List<Location> result = new ArrayList<Location>();
		
		Location landingSpot = target;
		while(true)
		{
			try
			{
				landingSpot = landingSpot.getRelativeLocation(player, direction);
				if(getBoard().isLocationFree(landingSpot))
				{
					result.add(landingSpot);
				}
				else
				{
					break; //cannot land on or fly past another piece
				}
			}
			catch(LocationOutOfRangeException ex)
			{
				break;
			}
		}
		
		return result;
	}
	
	private Location getPossibleSingleFlyCatchTargetLocationInDirection(Player player, Location start, Direction... direction)
	{
		Location target = start;
		while(true)
		{
			try
			{
				target = target.getRelativeLocation(player, direction);
				if(getBoard().isLocationOccupiedBy(player, target))
				{
					return null;
				}
				else if(getBoard().isLocationOccupiedBy(player.getOpponent(), target)) //potential target found
				{
					Location landingSpot = target.getRelativeLocation(player, direction);
					if(getBoard().isLocationFree(landingSpot))
					{
						return target;
					}
					else
					{
						return null;
					}
				}
				//else: location is free, continue
			}
			catch(LocationOutOfRangeException ex)
			{
				return null;
			}
		}
	}
}
