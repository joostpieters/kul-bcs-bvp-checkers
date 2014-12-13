package domain.square.contracts;
import domain.piece.contracts.IPiece;

public interface ISquare extends IReadOnlySquare
{
	public void clearPiece();
	
	public void setPiece(IPiece piece) throws IllegalStateException;
	
	public IReadOnlySquare getReadOnlySquare();
}
