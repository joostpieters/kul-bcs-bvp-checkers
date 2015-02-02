package domain.square.contracts;

import domain.board.contracts.IBoard;
import domain.piece.contracts.IPiece;

/**
 * Represents a square on an {@link IBoard}.
 * Can possibly contain {@link IPiece}s.
 * This square does not allow its contents to be altered.
 */
public interface IReadOnlySquare
{
	/**
	 * Returns true if this {@link IReadOnlySquare} contains an {@link IPiece}. 
	 */
	public boolean hasPiece();
	
	/**
	 * Returns the {@link IPiece} on this {@link IReadOnlySquare} if present,
	 * null otherwise.
	 * 
	 * @throws 	IllegalStateException
	 * 			If this Square does not contain a Piece.
	 */
	public IPiece getPiece() throws IllegalStateException;
}
