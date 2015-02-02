package domain.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import domain.board.contracts.IBoardSize;
import domain.square.contracts.ISquare;

/**
 * A special variant of {@link LocationPair} where both {@link Location}s
 * are forced to lie on the same diagonal.
 * 
 * @invariant super.isOnSameDiagonal()
 */
public class DiagonalLocationPair extends LocationPair
{
	/**
	 * Construct a new {@link DiagonalLocationPair} based on two {@link Location}s.
	 * @param 	from
	 * 			The from {@link Location}.
	 * @param 	to
	 * 			The to {@link Location}.
	 */
	public DiagonalLocationPair(Location from, Location to)
	{
		super(from, to);
		if(!super.isOnSameDiagonal())
		{
			throw new IllegalArgumentException("DiagonalLocationPair members must be on same diagonal.");
		}
	}
	
	/**
	 * Construct a new {@link DiagonalLocationPair} based on two location indices and an {@link IBoardSize}.
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
	public DiagonalLocationPair(int fromIndex, int toIndex, IBoardSize size) throws LocationOutOfRangeException
	{
		this(new Location(fromIndex, size), new Location(toIndex, size));
	}
	
	/**
	 * Returns the diagonal distance between the two {@link Location}s,
	 * which happens to coincide with both the row and the column distance.
	 */
	public int getDiagonalDistance()
	{
		return getRowDistance();
	}

	/**
	 * Returns the {@link Location} of the {@link ISquare} in the middle of this {@link DiagonalLocationPair}.
	 * This method only supports {@link DiagonalLocationPair}s with a diagonal distance of two. 
	 */
	public Location getCenterBetween()
	{
		if(getDiagonalDistance() != 2)
		{
			throw new IllegalArgumentException("Cannot calculate center if other location is diagonal distance is not 2.");
		}
		int centerRow = (getFrom().getRow() + getTo().getRow()) / 2;
		int centerCol = (getFrom().getCol() + getTo().getCol()) / 2;
		try
		{
			return new Location(centerRow, centerCol, getFrom().getBoardSize());
		}
		catch(LocationOutOfRangeException e)
		{
			assert(false);
			return null;
		}
	}
	
	/**
	 * Returns a list of {@link Location}s between - but excluding - the endpoints of this {@link DiagonalLocationPair}.
	 */
	public List<Location> getLocationsBetweenStrict()
	{
		Collection<Direction> direction = getUnitDirection();
		Stack<Location> locations = new Stack<Location>();
		Location current = new Location(getFrom());
		while(!current.equals(getTo()))
		{
			try
			{
				current = current.getRelativeLocation(null, direction); //move 1 step in direction of to
				locations.push(current);
			}
			catch(LocationOutOfRangeException e)
			{
				assert(false);
			}
		}
		locations.pop(); //top location == to
		return locations;
	}
	
	/**
	 * Returns a list of {@link Location}s between - and including - the endpoints of this {@link DiagonalLocationPair}.
	 */
	public List<Location> getLocationsBetween()
	{
		Collection<Direction> direction = getUnitDirection();
		Stack<Location> locations = new Stack<Location>();
		locations.push(getFrom());
		Location current = new Location(getFrom());
		while(!current.equals(getTo()))
		{
			try
			{
				current = current.getRelativeLocation(null, direction); //move 1 step in direction of to
				locations.push(current);
			}
			catch (LocationOutOfRangeException e)
			{
				assert(false);
			} 
		}
		return locations;
	}
	
	/**
	 * Returns a list of consecutive {@link DiagonalLocationPair}s (i.e. steps) of diagonal distance two 
	 * that, when added together, span all {@link Location}s strictly between this {@link DiagonalLocationPair}.  
	 */
	public List<DiagonalLocationPair> getStepsBetweenStrict()
	{
		List<Location> steps = getLocationsBetweenStrict();
		return getPairsBetween(steps);
	}
	
	/**
	 * Returns a list of consecutive {@link DiagonalLocationPair}s (i.e. steps) of diagonal distance two 
	 * that, when added together, span all {@link Location}s between this {@link DiagonalLocationPair}.  
	 */
	public List<DiagonalLocationPair> getStepsBetween()
	{
		List<Location> steps = getLocationsBetween();
		return getPairsBetween(steps);
	}
	
	private static List<DiagonalLocationPair> getPairsBetween(List<Location> steps)
	{
		List<DiagonalLocationPair> pairs = new ArrayList<DiagonalLocationPair>();
		for(int i=0; i < steps.size() - 1; i++)
		{
			Location stepFrom = steps.get(i);
			Location stepTo = steps.get(i+1);
			DiagonalLocationPair stepPair = new DiagonalLocationPair(stepFrom, stepTo);
			pairs.add(stepPair);
		}
		return pairs;
	}
	
	/**
	 * Returns true if the given {@link Location} lies strictly between this {@link DiagonalLocationPair}.
	 * @param 	location
	 * 			The {@link Location} to check.
	 */
	public boolean isBetweenStrict(Location location)
	{
		return getLocationsBetweenStrict().contains(location);
	}
	
	/**
	 * Returns true if the given {@link Location} lies between this {@link DiagonalLocationPair}.
	 * @param 	location
	 * 			The {@link Location} to check.
	 */
	public boolean isBetween(Location location)
	{
		return getLocationsBetween().contains(location);
	}
}
