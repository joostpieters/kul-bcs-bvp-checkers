package domain.action;

import java.util.List;

import common.Player;
import domain.board.Board;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.Square;

public class CompositeActionFly extends CompositeAction
{
	public CompositeActionFly(DiagonalLocationPair pair)
	{
		if(pair.getDiagonalDistance() < 2)
		{
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		List<DiagonalLocationPair> pairs = pair.getPairsBetweenInclusive();
		for(DiagonalLocationPair p : pairs)
		{
			Action subAction = new AtomicActionStep(p);
			addAction(subAction);
		}
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{		
		if(super.isValidOn(board, currentPlayer))
		{
			Location from = getFrom();
			Square fromSquare = board.getSquare(from);
			return fromSquare.getPiece().canFly(); //square now surely hasPiece
		}
		else
		{
			return false;
		}
	}
}
