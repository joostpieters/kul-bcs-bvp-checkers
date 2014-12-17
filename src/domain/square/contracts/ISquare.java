package domain.square.contracts;
import domain.piece.contracts.IPiece;

/**
 * Represents a Square on a Board.
 * Can possibly contain {@link IPiece}s.
 */
public interface ISquare extends IReadOnlySquare
{
	/**
	 * Clears the containing Piece from this Square (if present).
	 */
	public void clearPiece();
	
	/**
	 * Puts the given Piece on this Square.
	 * Possible overrides any currently contained Piece.
	 * 
	 * @param 	piece
	 * 			The Piece to add.
	 * 
	 * @throws 	IllegalStateException
	 * 			In case this Square cannot contain any Piece.
	 */
	public void setPiece(IPiece piece) throws IllegalStateException;
	
	/**
	 * Returns a read-only version of this Square.
	 */
	public IReadOnlySquare getReadOnlySquare();
}
