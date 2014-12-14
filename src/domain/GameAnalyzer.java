package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Configs;
import common.Player;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.action.ActionPriorityComparator;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.contracts.IBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;

public class GameAnalyzer extends GameUpdateSource {
	private static final ActionPriorityComparator ActionPriorityComparator = new ActionPriorityComparator();
	private final Game game;
	
	private Game getGame() {
		return game;
	}
	
	public GameAnalyzer(Game game) {
		this.game = game;
	}
	
	public void findAndPerformPromotions()
	{
		Player player = getGame().getCurrentPlayer();
		IBoard board = getGame().getBoard();
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(player))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					board.promotePiece(location);
					updateFollowers(board.getReadOnlyBoard());
				}
			}
		}
	}
	
//	public boolean isActionAllowed(ActionRequest request) //TODO move to Action.isValid?
//	{
//		return !Configs.MandatoryMaximalCatching || getAllowedActions().contains(request);
//	}
	
//	public List<ActionRequest> getAllowedActions()
//	{
//		IBoard board = getGame().getBoard();
//		List<ActionRequest> actions = getPossibleAtomicActions(board);
//		Collections.sort(actions, ActionPriorityComparator.reversed());
//		int highest = actions.get(0).getNumberOfCatches();
//		List<ActionRequest> possible = actions.stream().filter(a -> a.getNumberOfCatches() == highest).collect(Collectors.toList());
//		
//		return possible;
//	}
	
//	private List<ActionRequest> getPossibleActions()
//	{
//		IBoard testBoard = getGame().getBoard().getDeepClone();
//		Player currentPlayer = getGame().getCurrentPlayer();
//		List<ActionRequest> allActionRequests = new ArrayList<ActionRequest>();
//		Set<Location> locations = getGame().getBoard().getPlayerPieces(currentPlayer).keySet();
//		while(true)
//		{
//			List<ActionRequest> atomicActionRequests = getPossibleAtomicActions(testBoard, locations);
//			if(atomicActionRequests.size() == 0)
//			{
//				break;
//			}
//			allActionRequests.addAll(atomicActionRequests);
//			
//			Set<Location> nextLocations = new HashSet<Location>();
//			for(ActionRequest request : atomicActionRequests)
//			{
//				int fromIndex =request.getIndices().get(0);
//				int toIndex = request.getIndices().get(1);
//				Location from = new Location(fromIndex, testBoard.getSize());
//				Location to = new Location(toIndex, testBoard.getSize());
//				nextLocations.add(to);
//				DiagonalLocationPair pair = new DiagonalLocationPair(from, to);
//				Collection<Direction> direction = pair.getUnitDirection();
//				Predicate<DiagonalLocationPair> stepPredicate = p -> p.getUnitDirection().equals(direction);
//			}
//			
////			List<Action> validAtomicActions = atomicActionRequests.stream()
////					.map(request -> ActionFactory.create(request, testBoard, currentPlayer))
////					.filter(action -> action.isValidOn(testBoard, currentPlayer))
////					.collect(Collectors.toList());
////			
////			for(Action action : validAtomicActions)
////			{
////				action.executeOn(testBoard, currentPlayer);
////			}
//		}
//		
//		return allActionRequests;
//	}
	
	private List<CatchActionRequest> getPossibleCatchActions()
	{
		IBoard board = getGame().getBoard();
		Player currentPlayer = getGame().getCurrentPlayer();
		Set<Location> locations = getGame().getBoard().getPlayerPieces(currentPlayer).keySet();
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		for(Location location : locations)
		{
			List<CatchActionRequest> catchActionRequests = getCatchesFromLocation(board, location);
			result.addAll(catchActionRequests);
		}
		
		return result;
	}

	private List<CatchActionRequest> getCatchesFromLocation(IBoard board, Location location) {
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		List<AtomicCatchActionRequest> atomicCatchRequests = getAtomicCatchesFromLocation(board, location);
		for(AtomicCatchActionRequest atomicCatchRequest : atomicCatchRequests)
		{
			Location to = atomicCatchRequest.getEnd(board.getSize());
			List<CatchActionRequest> nextCatchRequests = getCatchesFromLocation(board, to);
			if(nextCatchRequests.size() == 0)
			{
				//TODO return what? (work with indices instead of requests?)
			}
			for(CatchActionRequest nextCatchRequest : nextCatchRequests)
			{
				CatchActionRequest combined = new CatchActionRequest(atomicCatchRequest, nextCatchRequest);
				result.add(combined);
			}
		}
		return result;
	}

	public boolean isCurrentPlayerOutOfMoves() //TODO validate actions first
	{
		IBoard board = getGame().getBoard();
		Player currentPlayer = getGame().getCurrentPlayer();
		Set<Location> locations = getGame().getBoard().getPlayerPieces(currentPlayer).keySet();
		return getPossibleAtomicActions(board, locations).size() == 0;
	}
	
	public void processCurrentPlayerOutOfMoves()
	{
		if(!isCurrentPlayerOutOfMoves())
		{
			throw new IllegalStateException("Current player is not out of moves.");
		}
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		Player winner = currentPlayer.getOpponent();
		game.getUI().outOfMoves(currentPlayer);
		game.gameOver(winner);
		updateFollowersGameOver(winner);
	}
	
	private List<ActionRequest> getPossibleAtomicActions(IBoard board, Set<Location> locations) //TODO include multi-range (recursive on actionable pieces -- but enforce flying straight!)
	{
		List<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
		for(Location location : locations)
		{
			List<MoveActionRequest> possibleAtomicSteps = getAtomicStepsFromLocation(board, location);
			List<AtomicCatchActionRequest> possibleAtomicCatches = getAtomicCatchesFromLocation(board, location);
			actionRequests.addAll(possibleAtomicSteps);
			actionRequests.addAll(possibleAtomicCatches);
		}
		
		return actionRequests;
	}

	private List<AtomicCatchActionRequest> getAtomicCatchesFromLocation(IBoard board, Location location)
	{
		List<AtomicCatchActionRequest> requests = new ArrayList<AtomicCatchActionRequest>();
		Player player = getGame().getCurrentPlayer();
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
	
	private List<MoveActionRequest> getAtomicStepsFromLocation(IBoard board, Location location) //TODO don't need to check steps -> always OK if no catches possible -> #steps > 0?
	{
		List<MoveActionRequest> requests = new ArrayList<MoveActionRequest>();
		Player player = getGame().getCurrentPlayer();
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
}
