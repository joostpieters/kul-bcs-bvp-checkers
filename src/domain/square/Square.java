package domain.square;
import domain.piece.Piece;

public abstract class Square
{
	public abstract boolean hasPiece();
	
	public abstract void clearPiece();
	
	public abstract Piece getPiece() throws IllegalStateException;

	public abstract void setPiece(Piece piece) throws IllegalStateException;
	
	@Override
	public abstract String toString();
}
