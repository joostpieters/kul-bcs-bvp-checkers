package domain.location;

import java.util.Collection;
import java.util.Stack;

import domain.board.contracts.IBoardSize;

/**
 * Represents an immutable pair of Locations: from and to.
 */
public class LocationPair
{
	private final Location from;
	private final Location to;
	
	/**
	 * Returns the from {@link Location}.
	 */
	public Location getFrom()
	{
		return from;
	}

	/**
	 * Returns the to {@link Location}.
	 */
	public Location getTo()
	{
		return to;
	}
	
	/**
	 * Construct a new {@link LocationPair} based on two {@link Location}s.
	 * 
	 * @param 	from
	 * 			The from {@link Location}.
	 * @param 	to
	 * 			The to {@link Location}.
	 */
	public LocationPair(Location from, Location to)
	{
		if(from == null || to == null)
		{
			throw new NullPointerException("Locations from and to must not be null.");
		}
		if(from.equals(to))
		{
			throw new IllegalArgumentException("Locations from and to must not be equal.");
		}
		if(!from.getBoardSize().equals(to.getBoardSize()))
		{
			throw new IllegalArgumentException("Location from and to must have the same board size.");
		}
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Construct a new {@link LocationPair} based on two location indices and an {@link IBoardSize}.
	 * 
	 * @param 	fromIndex
	 * 			The location index corresponding to the from {@link Location}.
	 * @param 	toIndex
	 * 			The location index corresponding to the to {@link Location}.
	 * @param 	size
	 * 			The {@link IBoardSize} to consider when converting location indices to {@link Location}s. 
	 * @throws 	LocationOutOfRangeException
	 * 			If the indices are invalid for the given {@link IBoardSize}.
	 */
	public LocationPair(int fromIndex, int toIndex, IBoardSize size) throws LocationOutOfRangeException
	{
		this(new Location(fromIndex, size), new Location(toIndex, size));
	}
	
	public LocationPair(LocationPair copy)
	{
		this(copy.getFrom(), copy.getTo());
	}
	
	/**
	 * Returns the distance in number of rows between this Pair.
	 */
	public int getRowDistance()
	{
		return Math.abs(getFrom().getRow()-getTo().getRow());
	}
	
	/**
	 * Returns the distance in number of columns between this Pair.
	 */
	public int getColumnDistance()
	{
		return Math.abs(getFrom().getCol()-getTo().getCol());
	}
	
	/**
	 * Returns true if from and to lie on the same diagonal, false otherwise. 
	 */
	public boolean isOnSameDiagonal()
	{
		return getRowDistance() == getColumnDistance();
	}
	
	/**
	 * Returns true if from and to lie on the same row, false otherwise.
	 */
	public boolean isOnSameRow()
	{
		return getFrom().getRow() == getTo().getRow();
	}
	
	/**
	 * Returns true if from and to lie on the same column, false otherwise.
	 */
	public boolean isOnSameColumn()
	{
		return getFrom().getCol() == getTo().getCol();
	}
	
	/**
	 * Returns one or two {@link Direction}s corresponding to the direction of the vector pointing from <code>from</code> to <code>to</code>.
	 * For example, if <code>to</code> lies north-east of <code>from</code>, this method will return {@link Direction#Above} and {@link Direction#Right}.
	 */
	public Collection<Direction> getUnitDirection()
	{
		Location from = getFrom();
		Location to = getTo();
		Stack<Direction> directions = new Stack<>();
		
		if(to.isAbove(from))
		{
			directions.push(Direction.Above);
		}
		else if(to.isBelow(from))
		{
			directions.push(Direction.Below);
		}
		
		if(to.isLeftFrom(from))
		{
			directions.push(Direction.Left);
		}
		else if(to.isRightFrom(from))
		{
			directions.push(Direction.Right);
		}
		return directions;
	}
	
	@Override
	public String toString()
	{
		return String.format("from %s to %s", getFrom(), getTo());
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
		if(obj instanceof LocationPair)
		{
			LocationPair casted = (LocationPair)obj;
			return 	this.getFrom().equals(casted.getFrom()) &&
					this.getTo().equals(casted.getTo());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return 37 * getFrom().hashCode() + getTo().hashCode(); 
	}
}
