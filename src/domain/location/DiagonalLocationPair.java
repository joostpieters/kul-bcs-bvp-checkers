package domain.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import domain.board.BoardSize;

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
	
	public DiagonalLocationPair(int fromIndex, int toIndex, BoardSize size)
	{
		super(fromIndex, toIndex, size);
		if(!super.isOnSameDiagonal())
		{
			throw new IllegalArgumentException("DiagonalLocationPair members must be on same diagonal.");
		}
	}
	
	@Override
	public boolean isOnSameDiagonal() {
		return true;
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
		return new Location(centerRow, centerCol, getFrom().getBoardSize());
	}
	
	public List<Location> getStepsBetweenExclusive()
	{
		Collection<Direction> direction = getUnitDirection();
		Stack<Location> locations = new Stack<Location>();
		Location current = new Location(getFrom());
		while(!current.equals(getTo()))
		{
			current = current.getRelativeLocation(null, direction); //move 1 step in direction of to
			locations.push(current);
		}
		locations.pop(); //top location == to
		return locations;
	}
	
	public List<Location> getStepsBetweenInclusive()
	{
		Collection<Direction> direction = getUnitDirection();
		Stack<Location> locations = new Stack<Location>();
		locations.push(getFrom());
		Location current = new Location(getFrom());
		while(!current.equals(getTo()))
		{
			current = current.getRelativeLocation(null, direction); //move 1 step in direction of to
			locations.push(current);
		}
		return locations;
	}
	
	public List<DiagonalLocationPair> getPairsBetweenExclusive()
	{
		List<Location> steps = getStepsBetweenExclusive();
		return getPairsBetween(steps);
	}
	
	public List<DiagonalLocationPair> getPairsBetweenInclusive()
	{
		List<Location> steps = getStepsBetweenInclusive();
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
	
	public boolean isBetweenInclusive(Location location)
	{
		return getStepsBetweenInclusive().contains(location);
	}
	
	public boolean isBetweenExclusive(Location location)
	{
		return getStepsBetweenExclusive().contains(location);
	}
}
