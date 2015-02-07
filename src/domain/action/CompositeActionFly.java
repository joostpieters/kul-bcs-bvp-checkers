package domain.action;

import java.util.List;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.contracts.IReadOnlySquare;

/**
 * Represents a {@link CompositeAction}, more specifically one comprised of multiple {@link AtomicActionStep} subactions.
 */
public class CompositeActionFly extends CompositeAction
{
	/**
	 * Creates a new {@link CompositeActionFly} based on the given {@link DiagonalLocationPair}.
	 * 
	 * @param 	pair
	 * 			The from and to {@link Location}s of this pair will determine the start- and endpoints of this {@link CompositeActionFly}.
	 */
	public CompositeActionFly(DiagonalLocationPair pair)
	{
		if(pair.getDiagonalDistance() < 2)
		{
			fireWarning("Distance is too short to fly.");
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		List<DiagonalLocationPair> pairs = pair.getStepsBetween();
		for(DiagonalLocationPair p : pairs)
		{
			IAction subAction = new AtomicActionStep(p);
			addSubAction(subAction);
		}
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link CompositeActionFly}'s are valid iff their substeps are valid when executed sequentially and the piece in question can fly.
	 */
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
