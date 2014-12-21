package domain.action;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

public class AtomicActionCatch extends AtomicAction
{
	public AtomicActionCatch(DiagonalLocationPair pair)
	{
		super(pair);
	}
	
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
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
		
		IReadOnlySquare fromSquare = board.getSquare(from);
		IReadOnlySquare centerSquare = board.getSquare(center);
		
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
		
		if(	!fromPiece.canCatchBackward() &&
			to.isBehind(from, currentPlayer))
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
		updateObserversExecuteAction(this);
		updateObserversBoard(board.getReadOnlyBoard(), currentPlayer);
	}
	
	@Override
	public String toString() {
		return String.format("Catch %s", getPair());
	}
}
