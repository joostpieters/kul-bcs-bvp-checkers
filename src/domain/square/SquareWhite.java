package domain.square;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

public class SquareWhite implements ISquare
{
	private final ReadOnlySquare readOnlySquare = new ReadOnlySquare(this);
	
	@Override
	public boolean hasPiece()
	{
		return false;
	}
	
	@Override
	public IPiece getPiece()
	{
		throw new IllegalStateException("White Squares do not contain pieces.");
	}

	@Override
	public void setPiece(IPiece piece)
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

	@Override
	public IReadOnlySquare getReadOnlySquare() {
		return readOnlySquare;
	}
}
