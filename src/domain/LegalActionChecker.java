package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import common.Configs;
import common.Player;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.action.ActionRequestPriorityComparator;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.CatchActionRequest;
import domain.board.contracts.IBoard;
import domain.location.Location;

public class LegalActionChecker
{
	private static final ActionRequestPriorityComparator ActionRequestPriorityComparator = new ActionRequestPriorityComparator();
	private final Game game;
	
	private Game getGame()
	{
		return game;
	}
	
	public LegalActionChecker(Game game)
	{
		this.game = game;
	}
	
	public boolean isActionLegal(ActionRequest request) //TODO move to Action.isValid?
	{
		List<ActionRequest> allowedCatchActions = getLegalCatchActions();
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
	
	public List<ActionRequest> getLegalCatchActions()
	{
		List<CatchActionRequest> actions = getPossibleCatchActions();
		if(actions.size() > 0)
		{
			Collections.sort(actions, ActionRequestPriorityComparator.reversed());
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
		List<AtomicCatchActionRequest> atomicCatchRequests = OutOfMovesChecker.getAtomicCatchesFromLocation(currentPlayer, board, start); //TODO find better place
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
}
