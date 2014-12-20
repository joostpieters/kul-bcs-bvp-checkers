package domain.action;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

public class AtomicActionStep extends AtomicAction
{
	public AtomicActionStep(DiagonalLocationPair pair)
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
		
		if(	pair.getDiagonalDistance() != 1) 
		{
			return false;
		}
		
		Location from = pair.getFrom();
		Location to = pair.getTo();
		IReadOnlySquare fromSquare = board.getSquare(from);
		IPiece fromPiece = fromSquare.getPiece();
		
		if(	to.isBehind(from, currentPlayer) &&
			!fromPiece.canStepBackward())
		{
			return false;
		}
		
		
		return fromPiece.getPlayer() == currentPlayer;
	}
	
	@Override
	public void executeOn(IBoard board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		board.movePiece(getPair());
		updateObserversExecuteAction(this);
		updateObserversBoard(board.getReadOnlyBoard(), currentPlayer);
	}
	
	@Override
	public String toString() {
		return String.format("Step %s", getPair());
	}
}
