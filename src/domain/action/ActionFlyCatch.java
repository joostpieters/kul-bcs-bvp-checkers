package domain.action;

import java.util.ArrayList;
import java.util.List;

import common.Location;
import common.Player;
import common.RestrictedLocationPair;
import domain.board.Board;
import domain.square.Square;

public class ActionFlyCatch extends Action
{
	private final RestrictedLocationPair pair;
	
	private RestrictedLocationPair getPair()
	{
		return pair;
	}
	
	public ActionFlyCatch(RestrictedLocationPair pair)
	{
		this.pair = pair;
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{		
		CompositeAction action = getCompositeAction();
		if(action.isValidOn(board, currentPlayer))
		{
			Location from = pair.getFrom();
			Square fromSquare = board.getSquare(from);
			return fromSquare.getPiece().canFly(); //square now surely hasPiece
		}
		else
		{
			return false;
		}
	}

	@Override
	public void executeOn(Board board, Player currentPlayer)
	{
		CompositeAction action = getCompositeAction();
		action.executeOn(board, currentPlayer);
	}
	
	private CompositeAction getCompositeAction()
	{
		RestrictedLocationPair pair = getPair();
		
		if(pair.getDiagonalDistance() <= 1) //ensures pairs below has minimum size 2
		{
			throw new IllegalStateException("Can only fly on diagonals and over more than one square");
		}
		
		List<RestrictedLocationPair> pairs = pair.getPairsBetweenInclusive();
		List<Action> actions = new ArrayList<>();
		for(int i=0; i < pairs.size() - 2; i++) //TODO can fly further
		{
			RestrictedLocationPair stepPair = pairs.get(i);
			Action step = new AtomicActionStep(stepPair);
			actions.add(step);
		}
		Location catchStart = pairs.get(pairs.size() - 2).getFrom();
		Location catchEnd = pairs.get(pairs.size() - 1).getTo();
		RestrictedLocationPair catchPair = new RestrictedLocationPair(catchStart, catchEnd);
		actions.add(new AtomicActionCatch(catchPair));
		return new CompositeAction(actions);
	}
	
	@Override
	public String toString() {
		return String.format("FlyCatch %s", getPair());
	}
}
