package domain.square;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

/**
 * Represents a White Square on a Board.
 * Cannot contain {@link Piece}s.
 */
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
	public String toString()
	{
		return "White Square";
	}

	@Override
	public IReadOnlySquare getReadOnlySquare()
	{
		return readOnlySquare;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		return obj instanceof SquareWhite;
	}
	
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(false); 
	}
}
