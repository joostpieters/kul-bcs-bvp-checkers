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
	
	public boolean isActionAllowed(ActionRequest request) //TODO move to Action.isValid?
	{
		List<ActionRequest> allowedCatchActions = getAllowedCatchActions();
		if(allowedCatchActions.size() > 0) //there is a (maximal) catch
		{
			if(Configs.MandatoryMaximalCatching) //request must be one of the maximal catches
			{
				return allowedCatchActions.contains(request);
			}
			else //request can be any catch
			{
				return request.isCatch();
			}
		}
		else //there are only valid move actions
		{
			if(request.isCatch())
			{
				throw new IllegalStateException("No catches possible, but CatchActionRequest given.");
			}
			return true;
		}
	}
	
	public List<ActionRequest> getAllowedCatchActions()
	{
		List<CatchActionRequest> actions = getPossibleCatchActions();
		if(actions.size() > 0)
		{
			Collections.sort(actions, ActionPriorityComparator.reversed());
			int highest = actions.get(0).getNumberOfCatches();
			List<ActionRequest> possible = actions.stream().filter(a -> a.getNumberOfCatches() == highest).collect(Collectors.toList());
			return possible;
		}
		return new ArrayList<ActionRequest>(0);
	}
	
	private List<CatchActionRequest> getPossibleCatchActions() //TODO include flycatches
	{
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		IBoard board = getGame().getBoard();
		Player currentPlayer = getGame().getCurrentPlayer();
		Set<Location> locations = getGame().getBoard().getPlayerPieces(currentPlayer).keySet();
		for(Location location : locations)
		{
			List<CatchActionRequest> catchActionRequests = getNextCatches(board, location);
			result.addAll(catchActionRequests);
		}
		
		return result;
	}
	
	private List<CatchActionRequest> getNextCatches(IBoard board, Location start)
	{
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		Player currentPlayer = getGame().getCurrentPlayer();
		List<AtomicCatchActionRequest> atomicCatchRequests = getAtomicCatchesFromLocation(board, start);
		for(AtomicCatchActionRequest atomicCatchRequest : atomicCatchRequests)
		{
			IBoard testBoard = board.getDeepClone();
			Action action = ActionFactory.create(atomicCatchRequest, testBoard, currentPlayer);
			if(action.isValidOn(testBoard, currentPlayer))
			{
				action.executeOn(testBoard, currentPlayer);
				List<CatchActionRequest> allNext = getNextCatches(testBoard, atomicCatchRequest.getEnd(testBoard.getSize()));
				if(allNext.size() == 0)
				{
					result.add(atomicCatchRequest);
				}
				else
				{
					for(CatchActionRequest next : allNext)
					{
						result.add(new CatchActionRequest(atomicCatchRequest, next));
					}
				}
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
	
	private List<ActionRequest> getPossibleAtomicActions(IBoard board, Set<Location> locations)
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
	
	private List<MoveActionRequest> getAtomicStepsFromLocation(IBoard board, Location location)
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
