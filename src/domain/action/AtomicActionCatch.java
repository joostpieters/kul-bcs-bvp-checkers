package domain.action;

import common.Player;
import domain.board.Board;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.Square;

public class AtomicActionCatch extends AtomicAction
{
	public AtomicActionCatch(DiagonalLocationPair pair)
	{
		super(pair);
	}
	
//	public AtomicActionCatch(AtomicActionStep step1, AtomicActionStep step2)
//	{
//		super(new DiagonalLocationPair(step1.getPair().getFrom(), step2.getPair().getTo()));
//	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{
		DiagonalLocationPair pair = getPair();
		Location from = pair.getFrom();
		Location to = pair.getTo();
		
		if(pair.getDiagonalDistance() != 2) //TODO !to.isInFrontOf(from, currentPlayer)
		{
			return false;
		}
		
		Location center = pair.getCenterBetween();
		Square fromSquare = board.getSquare(from);
		Square toSquare = board.getSquare(to);
		Square centerSquare = board.getSquare(center);
		
		if(	!fromSquare.hasPiece() || 
			!centerSquare.hasPiece() ||
			toSquare.hasPiece() ||
			fromSquare.getPiece().getPlayer() != currentPlayer || 
			centerSquare.getPiece().getPlayer()  == currentPlayer)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void executeOn(Board board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		DiagonalLocationPair pair = getPair();
		Location center = pair.getCenterBetween();
		board.removePiece(center);
		board.movePiece(pair);
	}
	
	@Override
	public String toString() {
		return String.format("Catch %s", getPair());
	}
}
