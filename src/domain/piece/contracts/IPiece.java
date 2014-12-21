package domain.piece.contracts;

import common.IDeepClonable;
import common.Player;

/**
 * Represents a Piece of the Game.
 * Each Piece has a {@link Player}.
 * Pieces can have various capabilities, 
 * as defined by the methods in this interface.  
 */
public interface IPiece extends IDeepClonable<IPiece>
{
	/**
	 * Returns the Player that owns this Piece.
	 */
	public abstract Player getPlayer();

	/**
	 * Returns a character representing the type and Player of this Piece.
	 */
	public abstract char getPieceCode();

	/**
	 * Returns true if this Piece can step backwards, false otherwise.
	 */
	public abstract boolean canStepBackward();

	/**
	 * Returns true if this Piece can catch backwards, false otherwise.
	 */
	public abstract boolean canCatchBackward();

	/**
	 * Returns true if this Piece can fly, false otherwise.
	 */
	public abstract boolean canFly();

	/**
	 * Returns true if this Piece can promote, false otherwise.
	 */
	public abstract boolean canPromote();

	/**
	 * Returns a clone of this Piece of the same type with the same Player.
	 */
	@Override
	public abstract IPiece getDeepClone();
}