package domain.board.contracts;

import domain.game.Game;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.ISquare;

/**
 * Represents the board this {@link Game} is played on. Every board has a number of {@link ISquare}s which can contain {@link IPiece}s.
 */
public interface IBoard extends IReadOnlyBoard
{
	/**
	 * Add an {@link IPiece} at the given {@link Location}.
	 */
	public void addPiece(Location location, IPiece piece);

	/**
	 * Remove an {@link IPiece} from the given {@link Location}.
	 */
	public void removePiece(Location location);

	/**
	 * Move an {@link IPiece} along a given {@link LocationPair}.
	 */
	public void movePiece(LocationPair pair);

	/**
	 * Returns the {@link ISquare} at the given {@link Location}.
	 */
	@Override
	public ISquare getSquare(Location location);
	
	/**
	 * Returns a read-only version of this {@link IBoard}.
	 */
	public IReadOnlyBoard getReadOnlyBoard();
}