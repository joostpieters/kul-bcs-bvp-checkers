package domain.action;

import common.Player;
import domain.board.Board;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.Square;

public class AtomicActionStep extends AtomicAction
{
	public AtomicActionStep(DiagonalLocationPair pair)
	{
		super(pair);
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{
		DiagonalLocationPair pair = getPair();
		Location from = pair.getFrom();
		Location to = pair.getTo();
		
		if(	pair.getDiagonalDistance() != 1 ||
			!to.isInFrontOf(from, currentPlayer))
		{
			return false;
		}
		
		Square fromSquare = board.getSquare(from);
		Square toSquare = board.getSquare(to);
		
		if(	!fromSquare.hasPiece() || 
			toSquare.hasPiece() ||
			fromSquare.getPiece().getPlayer() != currentPlayer)
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
		
		board.movePiece(getPair());
	}
	
	@Override
	public String toString() {
		return String.format("Step %s", getPair());
	}
}
