package domain.analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.action.request.ActionRequest;
import domain.action.request.ActionRequestPriorityComparator;
import domain.action.request.CatchActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;

/**
 * This {@link IAnalyser} find all maximal catches (i.e. those with the highest number of steps) on a given board.
 */
public class MaximalCatchActionAnalyser implements IAnalyser
{
	private final IReadOnlyBoard board;
	private static final Comparator<ActionRequest> ActionRequestPriorityComparator = new ActionRequestPriorityComparator().reversed();
	
	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}
	
	/**
	 * Constructs a new {@link MaximalCatchActionAnalyser} with the given {@link IReadOnlyBoard}.
	 * 
	 * @param 	board
	 * 			The {@link IReadOnlyBoard} to analyse.
	 */
	public MaximalCatchActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}
	
	/**
	 * Finds the maximal {@link CatchActionRequest}s the given player can choose from,
	 * using any of his {@link IPiece}s.
	 */
	@Override
	public List<CatchActionRequest> find(Player currentPlayer)
	{
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(getBoard());
		Collection<CatchActionRequest> catchRequests = analyser.find(currentPlayer);
		return findMaximalCatches(new ArrayList<CatchActionRequest>(catchRequests));
	}

	/**
	 * Finds the maximal {@link CatchActionRequest}s the given player can choose from, 
	 * using the {@link IPiece} at the given pieceLocation. 
	 */
	@Override
	public List<CatchActionRequest> find(Player currentPlayer, Location pieceLocation)
	{
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(getBoard());
		List<CatchActionRequest> actions = analyser.find(currentPlayer, pieceLocation);
		return findMaximalCatches(actions);
	}

	private List<CatchActionRequest> findMaximalCatches(List<CatchActionRequest> actions)
	{
		if(actions.size() == 0)
		{
			return new ArrayList<CatchActionRequest>(0);
		}
		Collections.sort(actions, ActionRequestPriorityComparator);
		int highest = actions.get(0).getNumberOfCatches();
		return actions.stream().filter(a -> a.getNumberOfCatches() == highest).collect(Collectors.toList());
	}
}
