package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import domain.board.BoardSize;

public class RestrictedLocationPair extends LocationPair
{
	
	public RestrictedLocationPair(Location from, Location to)
	{
		super(from, to);
		if(!isOnSameDiagonal())
		{
			throw new IllegalArgumentException("RestrictedLocationPair members must be on same diagonal.");
		}
	}
	
	public RestrictedLocationPair(int fromIndex, int toIndex, BoardSize size)
	{
		super(fromIndex, toIndex, size);
		if(!isOnSameDiagonal())
		{
			throw new IllegalArgumentException("RestrictedLocationPair members must be on same diagonal.");
		}
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
	
	public List<RestrictedLocationPair> getPairsBetweenExclusive()
	{
		List<Location> steps = getStepsBetweenExclusive();
		return getPairsBetween(steps);
	}
	
	public List<RestrictedLocationPair> getPairsBetweenInclusive()
	{
		List<Location> steps = getStepsBetweenInclusive();
		return getPairsBetween(steps);
	}
	
	private static List<RestrictedLocationPair> getPairsBetween(List<Location> steps)
	{
		List<RestrictedLocationPair> pairs = new ArrayList<RestrictedLocationPair>();
		for(int i=0; i < steps.size() - 1; i++)
		{
			Location stepFrom = steps.get(i);
			Location stepTo = steps.get(i+1);
			RestrictedLocationPair stepPair = new RestrictedLocationPair(stepFrom, stepTo);
			pairs.add(stepPair);
		}
		return pairs;
	}
}
