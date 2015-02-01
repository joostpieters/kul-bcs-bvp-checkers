package domain.location;

import java.util.Arrays;

import common.Player;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;

/**
 * An immutable class representing a location on an {@link IBoard} with 
 * a given {@link BoardSize}, specified by a given row and column index.
 */
public final class Location
{
	private final int row;
	private final int col;
	private final IBoardSize size;

	/**
	 * Creates a new {@link Location} with the given parameters.
	 * 
	 * @param 	row
	 * 			The row on the {@link IBoard}.
	 * @param 	col
	 * 			The column on the {@link IBoard}.
	 * @param 	size
	 * 			The size of the {@link IBoard}.
	 * @throws 	LocationOutOfRangeException
	 * 			When the given row and/or column is outside the given {@link IBoardSize}. 
	 */
	public Location(int row, int col, IBoardSize size) throws LocationOutOfRangeException
	{
		if(!size.isValidLocation(row, col))
		{
			throw new LocationOutOfRangeException(String.format("Location (%d, %d) is outside of the board size %s.", row, col, size));
		}
		this.row = row;
		this.col = col;
		this.size = size;
	}
	
	/**
	 * Creates a new {@link Location} with the given parameters.
	 * 
	 * @param 	index
	 *          The position of this location based on the index number of the
	 *          corresponding {@link BlackSquare}. Numbering starts at 1 in
	 *          the upper leftmost black square and ends in the lower
	 *          rightmost black square.
	 * @param 	size
	 * 			The size of the {@link IBoard}.
	 * @throws 	LocationOutOfRangeException
	 * 			When the given index is invalid on the given {@link IBoardSize}.
	 */
	public Location(int index, IBoardSize size) throws LocationOutOfRangeException
	{
		if(!size.isValidIndex(index))
		{
			throw new LocationOutOfRangeException(String.format("Invalid index %d for board size %s.", index, size));
		}
		index = index - 1; //zero-based indexing
		this.row = index * 2 / size.getCols();
		int col = (index % (size.getCols() / 2)) * 2;
		if(this.row % 2 == 0)
		{
			col++;
		}
		this.col = col;
		this.size = size;
	}
	
	/**
	 * Creates a new {@link Location} equal to the one given, 
	 * but without any links between the two.
	 * 
	 * @param 	location
	 * 			The {@link Location} to clone.
	 */
	public Location(Location location)
	{
		this.row = location.row;
		this.col = location.col;
		this.size = location.size;
	}

	/**
	 * Returns the row of this {@link Location}.
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Returns the column of this {@link Location}.
	 */
	public int getCol()
	{
		return col;
	}
	
	/**
	 * Returns the {@link BoardSize} related to this {@link Location}.
	 */
	public IBoardSize getBoardSize()
	{
		return size;
	}
	
	@Override
	public String toString()
	{
		if(isBlack())
		{
			return String.format("%d:(%d, %d)", getIndex(), getRow(), getCol());
		}
		else
		{
			return String.format("(%d, %d)", getRow(), getCol());
		}
	}
	
	/**
	 * Returns true if this corresponds to a white square.
	 */
	public boolean isWhite()
	{
		return (getRow() + getCol()) % 2 == 0;
	}

	/**
	 * Returns true if this corresponds to a black square.
	 */
	public boolean isBlack()
	{
		return !isWhite();
	}
	
	/**
	 * Returns the black square index of this {@link Location} as defined in {@link Location#Location(int, BoardSize)}.
	 */
	public int getIndex()
	{
		if(isWhite())
		{
			throw new IllegalStateException(String.format("Location %s corresponds to a white square, which does not have an index.", this));
		}
		
		int squareNumber = getRow() * getBoardSize().getCols() + getCol(); //[0 - M*N[ 
		return squareNumber / 2 + 1; //only count black squares
	}
	
	public boolean equalBoardSize(Location other)
	{
		return this.getBoardSize().equals(other.getBoardSize());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(this == obj) //same reference
		{
			return true;
		}
		if(obj instanceof Location)
		{
			Location casted = (Location)obj;
			return 	this.getRow() == casted.getRow() && 
					this.getCol() == casted.getCol() &&
					equalBoardSize(casted);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Integer.hashCode(getRow()) ^ Integer.hashCode(getCol()); 
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * on the promotion row of the given {@link Player},
	 * false otherwise.
	 * 
	 * @param 	player
	 * 			The {@link Player} whose promotion row to check.
	 */
	public boolean isOnPromotionRow(Player player)
	{
		return player == Player.Black ?
				getRow() == getBoardSize().getRows() - 1 :
				getRow() == 0;
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * above (i.e. North) of the other {@link Location},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 */
	public boolean isAbove(Location other)
	{
		return getRow() < other.getRow();
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * below (i.e. South) of the other {@link Location},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 */
	public boolean isBelow(Location other)
	{
		return getRow() > other.getRow();
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * left (i.e. West) of the other {@link Location},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 */
	public boolean isLeftFrom(Location other)
	{
		return getCol() < other.getCol();
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * right (i.e. East) of the other {@link Location},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 */
	public boolean isRightFrom(Location other)
	{
		return getCol() > other.getCol();
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * in front of the other {@link Location} from 
	 * the perspective of the given {@link Player},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 * @param	player
	 * 			The {@link Player} whose perspective to consider.
	 */
	public boolean isInFront(Location other, Player player)
	{
		int delta = getRow() - other.getRow();
		
		return player == Player.White ?
				delta < 0 :
				delta > 0;
	}
	
	/**
	 * Returns true if this {@link Location} lies 
	 * behind the other {@link Location} from 
	 * the perspective of the given {@link Player},
	 * false otherwise.
	 * 
	 * @param 	other
	 * 			The other {@link Location} to compare with.
	 * @param	player
	 * 			The {@link Player} whose perspective to consider.
	 */
	public boolean isBehind(Location other, Player player)
	{
		int delta = getRow() - other.getRow();
		
		return player == Player.White ?
				delta > 0 :
				delta < 0;
	}
	
	/**
	 * Returns the {@link Location} one row above this one.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getAbove() throws LocationOutOfRangeException
	{
		return new Location(getRow()-1, getCol(), getBoardSize());
	}
	
	/**
	 * Returns the {@link Location} one row below this one.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getBelow() throws LocationOutOfRangeException
	{
		return new Location(getRow()+1, getCol(), getBoardSize());
	}
	
	/**
	 * Returns the {@link Location} one row left of this one.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getLeft() throws LocationOutOfRangeException
	{
		return new Location(getRow(), getCol()-1, getBoardSize());
	}
	
	/**
	 * Returns the {@link Location} one row right of this one.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getRight() throws LocationOutOfRangeException
	{
		return new Location(getRow(), getCol()+1, getBoardSize());
	}
	
	/**
	 * Returns the {@link Location} one row in front of this one,
	 * from the given {@link Player}'s perspective.
	 * 
	 * @param	player
	 * 			The {@link Player} whose perspective to consider.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getFront(Player player) throws LocationOutOfRangeException
	{
		return player == Player.White ?
				getAbove() :
				getBelow();
	}
	
	/**
	 * Returns the {@link Location} one row behind this one,
	 * from the given {@link Player}'s perspective.
	 * 
	 * @param	player
	 * 			The {@link Player} whose perspective to consider.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getBack(Player player) throws LocationOutOfRangeException
	{
		return player == Player.White ?
				getBelow() :
				getAbove();
	}
	
	private Location getByDirection(Player player, Direction direction) throws LocationOutOfRangeException
	{
		switch(direction)
		{
			case Above: return getAbove();
			case Below: return getBelow();
			case Front: return getFront(player);
			case Back: return getBack(player);
			case Left: return getLeft();
			case Right: return getRight();
			default: throw new IllegalArgumentException("Unknown direction: " + direction);
		}
	}
	
	/**
	 * Returns the {@link Location} reached by following the given
	 * {@link Direction}s from the given {@link Player}'s perspective.
	 * 
	 * @param 	player
	 *        	The {@link Player} whose perspective to consider.
	 * @param 	steps
	 * 			The sequence of {@link Direction}'s to follow.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getRelativeLocation(Player player, Iterable<Direction> steps) throws LocationOutOfRangeException
	{
		Location current = this;
		for(Direction direction : steps)
		{
			current = current.getByDirection(player, direction);
		}
		return current;
	}
	
	/**
	 * Returns the {@link Location} reached by following the given
	 * {@link Direction}s from the given {@link Player}'s perspective.
	 * 
	 * @param 	player
	 *        	The {@link Player} whose perspective to consider.
	 * @param 	steps
	 * 			The sequence of {@link Direction}'s to follow.
	 * @throws LocationOutOfRangeException 
	 */
	public Location getRelativeLocation(Player player, Direction... steps) throws LocationOutOfRangeException
	{
		return getRelativeLocation(player, Arrays.asList(steps));
	}
}