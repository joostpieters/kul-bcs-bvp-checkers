package domain.board.contracts;

import domain.board.ReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.ISquare;

public interface IBoard extends IReadOnlyBoard {

	public void addPiece(Location location, IPiece piece);

	public void removePiece(Location location);

	public void movePiece(LocationPair pair);

	public void promotePiece(Location location);

	public IBoard getDeepClone();
	
	@Override
	public ISquare getSquare(Location location);
	
	public ReadOnlyBoard getReadOnlyBoard();
}