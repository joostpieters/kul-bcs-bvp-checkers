package domain.action;

import common.Location;
import common.LocationPair;
import common.Player;
import domain.board.Board;
import domain.square.Square;

public class AtomicActionCatch extends AtomicAction {
	public final static String REGEX = "(\\d+)\\s*x\\s*(\\d+)";
	
	public AtomicActionCatch(LocationPair pair) {
		super(pair);
	}
	
	public static boolean isValidPattern(String pattern)
	{
		return pattern.matches(REGEX);
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer) {
		LocationPair pair = getPair();
		Location from = pair.getFrom();
		Location to = pair.getTo();
		
		if(	!pair.isOnSameDiagonal() || 
			pair.getDiagonalDistance() != 2) //TODO !to.isInFrontOf(from, currentPlayer)
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
	public void executeOn(Board board, Player currentPlayer) {
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		LocationPair pair = getPair();
		Location center = pair.getCenterBetween();
		board.removePiece(center);
		board.movePiece(pair);
	}
	
	@Override
	public String toString() {
		return String.format("Catch %s", getPair());
	}
}
