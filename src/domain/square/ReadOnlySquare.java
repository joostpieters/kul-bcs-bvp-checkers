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
	
	public ReadOnlySquare(ISquare square)
	{
		this.square = square;
	}

	@Override
	public boolean hasPiece()
	{
		return getSquare().hasPiece();
	}

	@Override
	public IPiece getPiece() throws IllegalStateException
	{
		return getSquare().getPiece();
	}
	
//	@Override
//	public boolean canHavePiece()
//	{
//		return square.canHavePiece();
//	}
//	
//	@Override
//	public boolean isValidLocation(Location location)
//	{
//		return square.isValidLocation(location);
//	}
	
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
		if(obj instanceof IReadOnlySquare)
		{
			IReadOnlySquare casted = (IReadOnlySquare)obj;
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
		return square.hashCode();
	}
}
