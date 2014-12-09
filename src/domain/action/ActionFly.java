package domain.action;

import java.util.List;
import java.util.stream.Collectors;

import common.LocationPair;
import common.Player;
import domain.board.Board;

public class ActionFly extends Action
{
	public static final String REGEX = AtomicActionStep.REGEX;
	private final LocationPair pair;
	
	private LocationPair getPair()
	{
		return pair;
	}
	
	public ActionFly(LocationPair pair)
	{
		this.pair = pair;
	}
	
	public static boolean isValidPattern(String pattern)
	{
		return pattern.matches(REGEX);
	}

	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{		
		CompositeAction action = getCompositeAction();
		return action.isValidOn(board, currentPlayer);
	}

	@Override
	public void executeOn(Board board, Player currentPlayer)
	{
		CompositeAction action = getCompositeAction();
		action.executeOn(board, currentPlayer);
	}
	
	private CompositeAction getCompositeAction()
	{
		//TODO check CanFly somewhere
		LocationPair pair = getPair();
		
		if(	!pair.isOnSameDiagonal() || 
			pair.getDiagonalDistance() <= 1)
		{
			throw new IllegalStateException("Can only fly on diagonals and over more than one square");
		}
		
		List<LocationPair> pairs = pair.getPairsBetween(pair.getStepsBetweenInclusive());
		List<Action> actions = pairs.stream().map(p -> new AtomicActionStep(p)).collect(Collectors.toList());
		return new CompositeAction(actions);
	}
}
