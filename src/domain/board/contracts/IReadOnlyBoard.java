package domain.board.contracts;

import java.util.HashMap;

import common.IDeepClonable;
import common.Player;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

/**
 * Represents a read-only version of an {@link IBoard}.
 */
public interface IReadOnlyBoard extends IDeepClonable<IBoard>
{
	/**
	 * Returns the {@link IBoardSize} of this {@link IBoard}.
	 */
	public IBoardSize getSize();

	/**
	 * Returns a read-only version of the {@link ISquare} in the given {@link Location}.
	 */
	public IReadOnlySquare getSquare(Location location);

	/**
	 * Returns true if an {@link IPiece} can move on this {@link IBoard} along the given {@link LocationPair}, 
	 * false otherwise.
	 */
	public boolean isValidMove(LocationPair pair);

	/**
	 * Returns a lookup table of {@link IPiece}s of the given {@link Player} by {@link Location}. 
	 */
	public HashMap<Location, IPiece> getPlayerPieces(Player player);

	/**
	 * Returns true if the given {@link Location} does not containa {@link IPiece}, false otherwise.
	 */
	public boolean isLocationFree(Location target);

	/**
	 * Returns true if the given {@link Location} is occupied by an {@link IPiece} of the given {@link Player}.
	 */
	public boolean isLocationOccupiedBy(Player occupant, Location target);
}