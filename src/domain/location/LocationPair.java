package domain.location;

import java.util.Collection;
import java.util.Stack;

import domain.board.contracts.IBoardSize;

/**
 * Represents a pair of Locations: from and to.
 * 
 * @invariant getFrom() != null
 * @invariant getTo() != null
 * @invariant !getFrom().equals(getTo())
 * @invariant getFrom().getBoardSize().equals(getTo().getBoardSize())
 * @invariant this.equals(this)
 */
public class LocationPair
{
	private final Location from;
	private final Location to;
	
	public Location getFrom()
	{
		return from;
	}
	
	public Location getTo()
	{
		return to;
	}
	
	/**
	 * @pre from != null
	 * @pre to != null
	 * @pre !from.equals(to)
	 * @pre from.getBoardSize().equals(to.getBoardSize()
	 * 
	 * @post getFrom() == $pre(Location, from)
	 * @post getTo() == $pre(Location, to)
	 * 
	 * @param from
	 * @param to
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
	
	public LocationPair(int fromIndex, int toIndex, IBoardSize size)
	{
		this(new Location(fromIndex, size), new Location(toIndex, size));
	}
	
	public LocationPair(LocationPair copy)
	{
		this(copy.getFrom(), copy.getTo());
	}
	
	/**
	 * Returns the distance in number of rows between this Pair.
	 * 
	 * @post $result == Math.abs(getFrom().getRow()-getTo().getRow())
	 */
	public int getRowDistance()
	{
		return Math.abs(getFrom().getRow()-getTo().getRow());
	}
	
	/**
	 * Returns the distance in number of rows between this Pair.
	 * 
	 * @post $result == Math.abs(getFrom().getCol()-getTo().getCol())
	 */
	public int getColumnDistance()
	{
		return Math.abs(getFrom().getCol()-getTo().getCol());
	}
	
	public boolean isOnSameDiagonal()
	{
		return getRowDistance() == getColumnDistance();
	}
	
	public boolean isOnSameRow()
	{
		return getFrom().getRow() == getTo().getRow();
	}
	
	public boolean isOnSameColumn()
	{
		return getFrom().getCol() == getTo().getCol();
	}
	
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
		return getFrom().hashCode() ^ getTo().hashCode(); 
	}
}
