package domain.action;

import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.contracts.IReadOnlySquare;

public class CompositeActionFly extends CompositeAction
{
	public CompositeActionFly(DiagonalLocationPair pair)
	{
		if(pair.getDiagonalDistance() < 2)
		{
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		List<DiagonalLocationPair> pairs = pair.getStepsBetween();
		for(DiagonalLocationPair p : pairs)
		{
			IAction subAction = new AtomicActionStep(p);
			addAction(subAction);
		}
	}
	
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{		
		if(!super.isValidOn(board, currentPlayer))
		{
			return false;
		}
		Location from = getFrom();
		IReadOnlySquare fromSquare = board.getSquare(from);
		return fromSquare.getPiece().canFly(); //square now surely hasPiece
	}
}
