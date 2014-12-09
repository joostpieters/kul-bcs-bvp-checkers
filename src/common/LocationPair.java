package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import domain.board.BoardSize;

public class LocationPair {
	private final Location from;
	private final Location to;
	
	public Location getFrom() {
		return from;
	}
	public Location getTo() {
		return to;
	}
	
	public LocationPair(Location from, Location to) {
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
	
	public LocationPair(int fromIndex, int toIndex, BoardSize size)
	{
		this(new Location(fromIndex, size), new Location(toIndex, size));
	}
	
	public int getRowDistance()
	{
		return Math.abs(getFrom().getRow()-getTo().getRow());
	}
	
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
	
	public int getDiagonalDistance()
	{
		if(!isOnSameDiagonal())
		{
			throw new IllegalArgumentException("Given location is not on same diagonal.");
		}
		
		return getRowDistance();
	}
	
	public Location getCenterBetween()
	{
		if(	!isOnSameDiagonal() ||
			getDiagonalDistance() != 2)
		{
			throw new IllegalArgumentException("Cannot calculate center if other location is not on same diagonal or distance is not 2.");
		}
		int centerRow = (getFrom().getRow() + getTo().getRow()) / 2;
		int centerCol = (getFrom().getCol() + getTo().getCol()) / 2;
		return new Location(centerRow, centerCol, getFrom().getBoardSize());
	}
	
	public Collection<Direction> getUnitDirection()
	{
		Location from = getFrom();
		Location to = getTo();
		
		if(	!isOnSameColumn() &&
			!isOnSameRow() &&
			!isOnSameDiagonal())
		{
			throw new IllegalStateException("Cannot calculate unit direction if locations are not on same row, same column or same diagonal.");
		}
		
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
	
	public List<LocationPair> getPairsBetween(List<Location> steps)
	{
		List<LocationPair> pairs = new ArrayList<LocationPair>();
		for(int i=0; i < steps.size() - 1; i++)
		{
			Location stepFrom = steps.get(i);
			Location stepTo = steps.get(i+1);
			LocationPair stepPair = new LocationPair(stepFrom, stepTo);
			pairs.add(stepPair);
		}
		return pairs;
	}
	
	@Override
	public String toString() {
		return String.format("from %s to %s.", getFrom(), getTo());
	}
}
