package domain.action;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.Dame;
import domain.piece.Piece;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;
import domain.update.BasicUpdateSource;

/**
 * Represents a promotion action from {@link Piece} to {@link Dame}. 
 */
public class ActionPromotion extends BasicUpdateSource implements IAction
{
	private Location location;
	
	public Location getLocation()
	{
		return location;
	}

	private void setLocation(Location location)
	{
		this.location = location;
	}
	
	/**
	 * Creates a new {@link ActionPromotion} with the given {@link Location}.
	 * 
	 * @param 	location
	 * 			The {@link Location} of the {@link Piece} to promote.
	 */
	public ActionPromotion(Location location)
	{
		this.setLocation(location);
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link AtomicActionCatch}es are valid iff the following conditions are met:
	 * <ul>
	 * <li>The location of promotion contains a {@link Piece}.</li>
	 * <li>Said piece is of the current player.</li>
	 * <li>Said piece is promotable.</li>
	 * <li>The location of promotion is on the promotion row of the current player.</li>
	 * </ul>
	 */
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{
		IReadOnlySquare square = board.getSquare(getLocation());
		if(!square.hasPiece())
		{
			return false;
		}
		IPiece piece = square.getPiece();
		if(!piece.canPromote())
		{
			return false;
		}
		Player player = piece.getPlayer();
		if(player != currentPlayer)
		{
			return false;
		}
		return getLocation().isOnPromotionRow(player);
	}

	@Override
	public void executeOn(IBoard board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		ISquare square = board.getSquare(getLocation());
		IPiece piece = square.getPiece();
		Player player = piece.getPlayer();
		Dame dame = new Dame(player);
		board.removePiece(getLocation());
		board.addPiece(getLocation(), dame);
	}

	@Override
	public Location getFrom()
	{
		return getLocation();
	}

	@Override
	public boolean isCatch()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("Promotion at %s.", getLocation());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(this == obj)
		{
			return true;
		}
		if(obj instanceof ActionPromotion)
		{
			ActionPromotion casted = (ActionPromotion)obj;
			return getLocation().equals(casted.getLocation());
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return location.hashCode();
	}
}
