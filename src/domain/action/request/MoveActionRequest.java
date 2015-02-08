package domain.action.request;

import domain.location.Location;

public class MoveActionRequest extends ActionRequest
{
	public MoveActionRequest(Location from, Location to)
	{
		super(from, to);
	}
	
	@Override
	public String toString()
	{
		return String.format("%d-%d", getStart().getIndex(), getEnd().getIndex());
	}

	@Override
	public boolean isCatch()
	{
		return false;
	}

	@Override
	public int getNumberOfCatches()
	{
		return 0;
	}
}
