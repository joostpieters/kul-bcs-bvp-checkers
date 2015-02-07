package domain.action;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

/**
 * Represents a concrete {@link AtomicAction}, more specifically the atomic catch action.
 */
public class AtomicActionCatch extends AtomicAction
{
	/**
	 * Creates a new {@link AtomicActionCatch} based on the given {@link DiagonalLocationPair}.
	 * 
	 * @param 	pair
	 * 			The from and to {@link Location}s of this pair will determine the start- and endpoints of this {@link AtomicActionCatch}.
	 */
	public AtomicActionCatch(DiagonalLocationPair pair)
	{
		super(pair);
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link AtomicActionCatch}es are valid iff the following conditions are fulfilled:
	 * <ul>
	 * <li>Both starting and landing square are black</li>
	 * <li>The starting square contains a piece of the current player</li>
	 * <li>The landing square is empty</li>
	 * <li>The starting and landing squares are exactly a diagonal distance of two apart</li>
	 * <li>The square in the center between start and end contains a piece from the opponent</li>
	 * <li>If the landing square lies behind the starting square, backwards catching must be enabled</li>
	 * </ul>
	 */
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
		emitExecuteAction(this);
		emitUpdateBoard(board.getReadOnlyBoard(), currentPlayer);
	}
	
	@Override
	public String toString() {
		return String.format("Catch %s", getPair());
	}

	@Override
	public boolean isCatch()
	{
		return true;
	}
}
