package domain.square;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

/**
 * Represents a Black Square on a Board.
 * Can contain {@link Piece}s.
 */
public class SquareBlack implements ISquare
{
	private IPiece piece;
	private final ReadOnlySquare readOnlySquare = new ReadOnlySquare(this);
	
	public SquareBlack()
	{
		this(null);
	}
	
	public SquareBlack(IPiece piece)
	{
		this.piece = piece;
	}
	
	public boolean hasPiece()
	{
		return piece != null;
	}
	
	public IPiece getPiece()
	{
		return piece;
	}

	public void setPiece(IPiece piece)
	{
		this.piece = piece;
	}
	
	public void clearPiece()
	{
		this.piece = null;
	}

	@Override
	public String toString()
	{
		if(hasPiece())
		{
			return "Black Square containing " + getPiece();
		}
		return "Black Square";
	}

	@Override
	public IReadOnlySquare getReadOnlySquare()
	{
		return readOnlySquare;
	}
}
