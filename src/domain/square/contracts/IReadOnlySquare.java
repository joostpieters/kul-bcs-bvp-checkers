package domain.square.contracts;

import domain.piece.contracts.IPiece;

/**
 * Represents a Square on a Board.
 * Can possibly contain {@link IPiece}s.
 * This Square does not allow its contents to be altered.
 */
public interface IReadOnlySquare
{
	/**
	 * Returns true if this Square contains a Piece. 
	 */
	public boolean hasPiece();
	
	/**
	 * Returns the Piece on this Square if present.
	 * Null otherwise.
	 * 
	 * @throws 	IllegalStateException
	 * 			If this Square does not contain a Piece.
	 */
	public IPiece getPiece() throws IllegalStateException;
}
