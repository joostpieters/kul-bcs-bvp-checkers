package domain.action;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.Dame;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

/**
 * Represents a concrete {@link AtomicAction}, more specifically the atomic step action.
 */
public class AtomicActionStep extends AtomicAction
{
	/**
	 * Creates a new {@link AtomicActionStep} based on the given {@link DiagonalLocationPair}.
	 * 
	 * @param 	pair
	 * 			The from and to {@link Location}s of this pair will determine the start- and endpoints of this {@link AtomicActionStep}.
	 */
	public AtomicActionStep(DiagonalLocationPair pair)
	{
		super(pair);
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link AtomicActionStep}es are valid iff the following conditions are fulfilled:
	 * <ul>
	 * <li>Both starting and landing square are black</li>
	 * <li>The starting square contains a piece of the current player</li>
	 * <li>The landing square is empty</li>
	 * <li>The starting and landing squares are exactly a diagonal distance of one apart</li>
	 * <li>If the landing square lies behind the starting square, the piece in question must be a {@link Dame}.</li>
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
		emitExecuteAction(this);
		emitUpdateBoard(board.getReadOnlyBoard(), currentPlayer);
	}
	
	@Override
	public String toString()
	{
		return String.format("Step %s", getPair());
	}

	@Override
	public boolean isCatch()
	{
		return false;
	}
}
