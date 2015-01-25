package domain.board.contracts;

import java.util.HashMap;

import common.IDeepClonable;
import common.Player;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

public interface IReadOnlyBoard extends IDeepClonable<IBoard>
{
	public IBoardSize getSize();

	public Location createLocation(int row, int col);

	public IReadOnlySquare getSquare(Location location);

	public boolean isValidMove(LocationPair pair);

	public HashMap<Location, IPiece> getPlayerPieces(Player player);

	public boolean isLocationFree(Location target);

	public boolean isLocationOccupiedBy(Player occupant, Location target);
}