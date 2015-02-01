package domain.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import domain.board.contracts.IBoardSize;

/**
 * @invariant super.isOnSameDiagonal()
 */
public class DiagonalLocationPair extends LocationPair
{
	public DiagonalLocationPair(Location from, Location to)
	{
		super(from, to);
		if(!super.isOnSameDiagonal())
		{
			throw new IllegalArgumentException("DiagonalLocationPair members must be on same diagonal.");
		}
	}
	
	public DiagonalLocationPair(int fromIndex, int toIndex, IBoardSize size) throws LocationOutOfRangeException
	{
		this(new Location(fromIndex, size), new Location(toIndex, size));
	}
	
	public int getDiagonalDistance()
	{
		return getRowDistance();
	}
	
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
	
	public List<Location> getStepsBetweenStrict()
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
	
	public List<Location> getStepsBetween()
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
	
	public List<DiagonalLocationPair> getPairsBetweenStrict()
	{
		List<Location> steps = getStepsBetweenStrict();
		return getPairsBetween(steps);
	}
	
	public List<DiagonalLocationPair> getPairsBetween()
	{
		List<Location> steps = getStepsBetween();
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
	
	public boolean isBetweenStrict(Location location)
	{
		return getStepsBetweenStrict().contains(location);
	}
	
	public boolean isBetween(Location location)
	{
		return getStepsBetween().contains(location);
	}
}
