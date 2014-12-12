package domain.location;

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
	
	@Override
	public String toString() {
		return String.format("from %s to %s", getFrom(), getTo());
	}
}
