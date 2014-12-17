package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import common.Player;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.updates.GameUpdateSource;
import domain.updates.contracts.IGameFollower;

public class OutOfMovesChecker extends GameUpdateSource implements IGameFollower
{
	
	@Override
	public void update(IReadOnlyBoard board, Player performer)
	{
		Player nextPlayer = performer.getOpponent();
		if(isCurrentPlayerOutOfMoves(nextPlayer, board))
		{
			updateFollowersOutOfMoves(nextPlayer);
		}
	}
	
	@Override
	public void promotion(Location location) { }
	
	@Override
	public void gameOver(Player winner) { }
	
	@Override
	public void outOfMoves(Player player)
	{
		//self-generated, ignore
	}
	
	public static boolean isCurrentPlayerOutOfMoves(Player player, IReadOnlyBoard board)
	{
		Set<Location> locations = board.getPlayerPieces(player).keySet();
		for(Location location : locations)
		{
			if(canAct(player, board, location))
			{
				return false;
			}
		}
		return true;
	}
	
	private static <T extends ActionRequest> List<T> filterValidActionRequests(Player player, IReadOnlyBoard board, List<T> requests)
	{
		List<T> filteredRequests = new ArrayList<T>();
		for(T step : requests)
		{
			Action action = ActionFactory.create(step, board, player);
			if(action.isValidOn(board, player)) //no need to execute in case of atomic actions
			{
				filteredRequests.add(step);
			}
		}
		
		return filteredRequests;
	}
	
	private static boolean canMove(Player player, IReadOnlyBoard board, Location location)
	{
		List<MoveActionRequest> possibleAtomicSteps = getAtomicStepsFromLocation(player, board, location);		
		List<MoveActionRequest> validAtomicSteps = filterValidActionRequests(player, board, possibleAtomicSteps);
		
		return validAtomicSteps.size() > 0;
	}
	
	private static boolean canCatch(Player player, IReadOnlyBoard board, Location location)
	{
		List<AtomicCatchActionRequest> possibleAtomicCatches = getAtomicCatchesFromLocation(player, board, location);		
		List<AtomicCatchActionRequest> validAtomicCatches = filterValidActionRequests(player, board, possibleAtomicCatches);
		
		return validAtomicCatches.size() > 0;
	}

	private static boolean canAct(Player player, IReadOnlyBoard board, Location location)
	{
		return canMove(player, board, location) || canCatch(player, board, location);
	}
	
	public static List<MoveActionRequest> getAtomicStepsFromLocation(Player player, IReadOnlyBoard board, Location location)
	{
		List<MoveActionRequest> requests = new ArrayList<MoveActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		for(Location target : targets)
		{
			if(	board.isLocationOccupiedBy(player, location) &&
				board.isLocationFree(target))
			{
				requests.add(new MoveActionRequest(location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
	
	public static List<AtomicCatchActionRequest> getAtomicCatchesFromLocation(Player player, IReadOnlyBoard board, Location location)
	{
		List<AtomicCatchActionRequest> requests = new ArrayList<AtomicCatchActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		
		for(Location target : targets)
		{
			DiagonalLocationPair pair = new DiagonalLocationPair(location, target);
			if(	board.isLocationOccupiedBy(player, location) &&
					board.isLocationFree(target) && 
					board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				requests.add(new AtomicCatchActionRequest(location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
}
