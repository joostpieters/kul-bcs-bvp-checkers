package domain.board;

import java.util.HashMap;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

/**
 * Provides a read-only wrapper around Board that implements a limited interface.
 */
public class ReadOnlyBoard implements IReadOnlyBoard
{
	private final IBoard board;
	
	private IBoard getBoard()
	{
		return board;
	}
	
	public ReadOnlyBoard(IBoard board)
	{
		this.board = board;		
	}

	@Override
	public BoardSize getSize() {
		return getBoard().getSize();
	}

	@Override
	public Location createLocation(int row, int col) {
		return getBoard().createLocation(row, col);
	}

	@Override
	public IReadOnlySquare getSquare(Location location) {
		return getBoard().getSquare(location).getReadOnlySquare();
	}

	@Override
	public boolean isValidMove(LocationPair pair) {
		return getBoard().isValidMove(pair);
	}

	@Override
	public HashMap<Location, IPiece> getPlayerPieces(Player player) {
		return getBoard().getPlayerPieces(player);
	}

	@Override
	public boolean isLocationFree(Location target) {
		return getBoard().isLocationFree(target);
	}

	@Override
	public boolean isLocationOccupiedBy(Player occupant, Location target) {
		return getBoard().isLocationOccupiedBy(occupant, target);
	}

	@Override
	public String toString() {
		return getBoard().toString();
	}

	@Override
	public IBoard getDeepClone() {
		return getBoard().getDeepClone();
	}
}
