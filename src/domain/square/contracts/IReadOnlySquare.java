package domain.square.contracts;

import domain.piece.contracts.IPiece;

public interface IReadOnlySquare
{
	public boolean hasPiece();
	
	public IPiece getPiece() throws IllegalStateException;
}
