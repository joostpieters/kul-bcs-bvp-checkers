package domain.action;

import common.Player;
import domain.board.contracts.IBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.square.contracts.ISquare;

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
	public boolean isValidOn(IBoard board, Player currentPlayer)
	{
		DiagonalLocationPair pair = getPair();
		if(!board.isValidMove(pair))
		{
			return false;
		}
		
		if(pair.getDiagonalDistance() != 2)
		{
			return false;
		}
		
		Location from = pair.getFrom();
		Location center = pair.getCenterBetween();
		Location to = pair.getTo();
		
		ISquare fromSquare = board.getSquare(from);
		ISquare centerSquare = board.getSquare(center);
		
		if(!centerSquare.hasPiece())
		{
			return false;
		}

		IPiece fromPiece = fromSquare.getPiece();
		IPiece centerPiece = centerSquare.getPiece();
		
		
		if(	fromPiece.getPlayer() != currentPlayer || 
			centerPiece.getPlayer()  == currentPlayer)
		{
			return false;
		}
		
		if(	to.isBehind(from, currentPlayer) &&
			!fromPiece.canCatchBackward())
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void executeOn(IBoard board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		DiagonalLocationPair pair = getPair();
		Location center = pair.getCenterBetween();
		board.removePiece(center);
		board.movePiece(pair);
		updateFollowers(board.getReadOnlyBoard());
	}
	
	@Override
	public String toString() {
		return String.format("Catch %s", getPair());
	}
}
