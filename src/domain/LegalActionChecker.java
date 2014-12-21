package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import common.Configs;
import common.Player;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.request.ActionRequest;
import domain.action.request.ActionRequestPriorityComparator;
import domain.action.request.CatchActionRequest;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IReadOnlyGame;
import domain.location.Direction;
import domain.location.Location;
import domain.observers.OutOfMovesObserver;
import domain.piece.contracts.IPiece;

public class LegalActionChecker
{
	private static final ActionRequestPriorityComparator ActionRequestPriorityComparator = new ActionRequestPriorityComparator();
	private final IReadOnlyGame game;
	
	private IReadOnlyGame getGame()
	{
		return game;
	}
	
	public LegalActionChecker(IReadOnlyGame game)
	{
		this.game = game;
	}
	
	public boolean isActionLegal(ActionRequest request) //TODO move to Action.isValid?
	{
		if(Configs.MandatoryMaximalCatching) //request must be one of the maximal catches
		{
			List<CatchActionRequest> maximalCatchActions = getMaximalCatchActions();
			if(maximalCatchActions.size() > 0) //there is a (maximal) catch
			{
				return maximalCatchActions.contains(request);
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
		else //request can be any catch
		{
			return request.isCatch();
		}
	}
	
	//get possible actions and return those with the highest number of catches
	private List<CatchActionRequest> getMaximalCatchActions()
	{
		List<CatchActionRequest> actions = getPossibleCatchActions();
		if(actions.size() > 0)
		{
			Collections.sort(actions, ActionRequestPriorityComparator.reversed());
			int highest = actions.get(0).getNumberOfCatches();
			List<CatchActionRequest> legalActions = actions.stream().filter(a -> a.getNumberOfCatches() == highest).collect(Collectors.toList());
			return legalActions;
		}
		return new ArrayList<CatchActionRequest>(0);
	}
	
	private List<CatchActionRequest> getPossibleCatchActions()
	{
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		IReadOnlyBoard board = getGame().getBoard();
		Player currentPlayer = getGame().getCurrentPlayer();
		HashMap<Location,IPiece> pieceLocations = getGame().getBoard().getPlayerPieces(currentPlayer);
		for(Location location : pieceLocations.keySet())
		{
			boolean includeFly = pieceLocations.get(location).canFly();
			List<CatchActionRequest> catchActionRequests = getNextCatches(board, location, includeFly);
			result.addAll(catchActionRequests);
		}
		
		return result;
	}
	
	private List<CatchActionRequest> getNextCatches(IReadOnlyBoard board, Location start, boolean includeFly)
	{
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		Player currentPlayer = getGame().getCurrentPlayer();
		List<CatchActionRequest> catchRequests = new ArrayList<CatchActionRequest>();
		catchRequests.addAll(includeFly
			? getPossibleSingleFlyCatchActionsFromLocation(board, currentPlayer, start)
			: OutOfMovesObserver.getAtomicCatchesFromLocation(board, currentPlayer, start));
		//OutOfMovesObserver.getAtomicCatchesFromLocation(board, currentPlayer, start);
		for(CatchActionRequest catchRequest : catchRequests)
		{
			IBoard testBoard = board.getDeepClone();
			IAction action = ActionFactory.create(catchRequest, testBoard, currentPlayer);
			if(action.isValidOn(testBoard, currentPlayer))
			{
				action.executeOn(testBoard, currentPlayer);
				List<CatchActionRequest> allNext = getNextCatches(testBoard, catchRequest.getEnd(testBoard.getSize()), includeFly);
				if(allNext.size() == 0)
				{
					result.add(catchRequest);
				}
				else
				{
					for(CatchActionRequest next : allNext)
					{
						result.add(new CatchActionRequest(catchRequest, next));
					}
				}
			}
		}
		
		return result;
	}
	
	private static Location getPossibleSingleFlyCatchTargetLocationInDirection(IReadOnlyBoard board, Player player, Location start, Direction... direction)
	{
		Location target = start;
		while(true)
		{
			try
			{
				target = target.getRelativeLocation(player, direction);
				if(board.isLocationOccupiedBy(player, target))
				{
					return null;
				}
				else if(board.isLocationOccupiedBy(player.getOpponent(), target)) //potential target found
				{
					Location landingSpot = target.getRelativeLocation(player, direction);
					if(board.isLocationFree(landingSpot))
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
			catch(IllegalArgumentException ex)
			{
				return null; //reached the end of the board
			}
		}
	}
	
	private static List<Location> getPossibleSingleFlyCatchLandingLocationsInDirection(IReadOnlyBoard board, Player player, Location target, Direction... direction)
	{
		List<Location> result = new ArrayList<Location>();
		
		Location landingSpot = target;
		while(true)
		{
			try
			{
				landingSpot = landingSpot.getRelativeLocation(player, direction);
				if(board.isLocationFree(landingSpot))
				{
					result.add(landingSpot);
				}
				else
				{
					break; //cannot land on or fly past another piece
				}
			}
			catch(IllegalArgumentException ex)
			{
				break; //reached end of board
			}
		}
		
		return result;
	}
	
	public static List<CatchActionRequest> getPossibleSingleFlyCatchActionsFromLocation(IReadOnlyBoard board, Player player, Location start)
	{
		List<CatchActionRequest> requests = new ArrayList<CatchActionRequest>();
		
		for(Direction[] direction : Direction.getDiagonalDirections())
		{
			Location target = getPossibleSingleFlyCatchTargetLocationInDirection(board, player, start, direction);
			if(target != null)
			{
				List<Location> landingSpots = getPossibleSingleFlyCatchLandingLocationsInDirection(board, player, target, direction);
				for(Location landingSpot : landingSpots)
				{
					CatchActionRequest catchActionRequest = new CatchActionRequest(start.getIndex(), landingSpot.getIndex());
					requests.add(catchActionRequest);
				}
			}
		}
		
		return requests;
	}
}
