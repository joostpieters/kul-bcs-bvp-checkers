package domain.square;
import domain.piece.Piece;


public class SquareBlack extends Square
{
	private Piece piece;

	public SquareBlack()
	{
		this(null);
	}
	
	public SquareBlack(Piece piece)
	{
		this.piece = piece;
	}
	
	public boolean hasPiece()
	{
		return piece != null;
	}
	
	public Piece getPiece()
	{
		return piece;
	}

	public void setPiece(Piece piece)
	{
		this.piece = piece;
	}
	
	public void clearPiece()
	{
		this.piece = null;
	}
}
