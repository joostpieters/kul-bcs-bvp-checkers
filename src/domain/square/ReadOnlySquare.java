package domain.square;

import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

/**
 * Represents a Square on a Board.
 * Can possibly contain {@link IPiece}s.
 * This Square does not allow its contents to be altered.
 */
public class ReadOnlySquare implements IReadOnlySquare
{
	private final ISquare square;
	
	private ISquare getSquare()
	{
		return square;
	}
	
	public ReadOnlySquare(ISquare square) {
		this.square = square;
	}

	@Override
	public boolean hasPiece() {
		return getSquare().hasPiece();
	}

	@Override
	public IPiece getPiece() throws IllegalStateException {
		return getSquare().getPiece();
	}
}
