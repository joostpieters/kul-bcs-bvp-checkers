package domain.piece.contracts;

import common.IDeepClonable;
import common.Player;

/**
 * Represents a piece on the {@link IBoard}. Each {@link IPiece} has a {@link Player}.
 * Pieces can have various capabilities, as defined by the methods in this interface.  
 */
public interface IPiece extends IDeepClonable<IPiece>
{
	/**
	 * Returns the Player that owns this Piece.
	 */
	public abstract Player getPlayer();

	/**
	 * Returns a character representing the type and {@link Player} of this Piece.
	 */
	public abstract char getPieceCode();

	/**
	 * Returns true if this {@link IPiece} can step backwards, false otherwise.
	 */
	public abstract boolean canStepBackward();

	/**
	 * Returns true if this {@link IPiece} can catch backwards, false otherwise.
	 */
	public abstract boolean canCatchBackward();

	/**
	 * Returns true if this {@link IPiece} can fly, false otherwise.
	 */
	public abstract boolean canFly();

	/**
	 * Returns true if this {@link IPiece} can promote, false otherwise.
	 */
	public abstract boolean canPromote();

	/**
	 * Returns a clone of this {@link IPiece} of the same type with the same {@link Player}.
	 */
	@Override
	public abstract IPiece getDeepClone();
}