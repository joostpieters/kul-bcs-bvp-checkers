package domain.square;
import domain.piece.Piece;

public class SquareWhite extends Square
{
	@Override
	public boolean hasPiece()
	{
		return false;
	}
	
	@Override
	public Piece getPiece()
	{
		throw new IllegalStateException("White Squares do not contain pieces.");
	}

	@Override
	public void setPiece(Piece piece)
	{
		throw new IllegalStateException("White Squares cannot contain pieces.");
	}

	@Override
	public void clearPiece()
	{
		throw new IllegalStateException("White Squares cannot contain pieces.");
	}
	
	@Override
	public String toString() {
		return "White Square";
	}
}
