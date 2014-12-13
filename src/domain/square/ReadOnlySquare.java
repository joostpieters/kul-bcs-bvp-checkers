package domain.square;

import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

/**
 * Provides a read-only wrapper around Square that implements a limited interface.
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
