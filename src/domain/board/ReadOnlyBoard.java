package domain.board;

import java.util.HashMap;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

/**
 * Provides a read-only wrapper around Board that implements a limited
 * interface.
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
		if(board == null)
		{
			throw new NullPointerException("Board must not be null.");
		}
		this.board = board;
	}

	@Override
	public IBoardSize getSize()
	{
		return getBoard().getSize();
	}

	@Override
	public Location createLocation(int row, int col)
	{
		return getBoard().createLocation(row, col);
	}

	@Override
	public IReadOnlySquare getSquare(Location location)
	{
		return getBoard().getSquare(location).getReadOnlySquare();
	}

	@Override
	public boolean isValidMove(LocationPair pair)
	{
		return getBoard().isValidMove(pair);
	}

	@Override
	public HashMap<Location, IPiece> getPlayerPieces(Player player)
	{
		return getBoard().getPlayerPieces(player);
	}

	@Override
	public boolean isLocationFree(Location target)
	{
		return getBoard().isLocationFree(target);
	}

	@Override
	public boolean isLocationOccupiedBy(Player occupant, Location target)
	{
		return getBoard().isLocationOccupiedBy(occupant, target);
	}

	@Override
	public String toString()
	{
		return getBoard().toString();
	}

	@Override
	public IBoard getDeepClone()
	{
		return new Board(getBoard());
	}

	@Override
	public int hashCode()
	{
		return board.hashCode();
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
		if(obj instanceof ReadOnlyBoard)
		{
			IReadOnlyBoard casted = (IReadOnlyBoard)obj;
			if (!this.getSize().equals(casted.getSize()))
			{
				return false;
			}
			for(int row=0; row < getSize().getRows(); row++)
			{
				for(int col=0; col < getSize().getCols(); col++)
				{
					Location location = new Location(row, col, getSize());
					if(!this.getSquare(location).equals(casted.getSquare(location)))
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
