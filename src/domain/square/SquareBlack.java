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
	
	/**
	 * Creates an empty Black Square.
	 */
	public SquareBlack()
	{
		this(null);
	}
	
	/**
	 * Creates a Black Square containing the given {@link IPiece}.
	 * 
	 * @param 	piece
	 * 			The piece to add to this Square.
	 */
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
		if(!hasPiece())
		{
			throw new IllegalStateException("Square has no Piece.");
		}
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
		if(obj instanceof SquareBlack)
		{
			SquareBlack casted = (SquareBlack)obj;
			if(this.hasPiece() != casted.hasPiece())
			{
				return false;
			}
			if(this.hasPiece()) //both have pieces
			{
				return this.getPiece().equals(casted.getPiece());
			}
			//both have no pieces
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int pieceHash = hasPiece() ? getPiece().hashCode() : 0;
		return 37 * pieceHash + Boolean.hashCode(true); 
	}
}
