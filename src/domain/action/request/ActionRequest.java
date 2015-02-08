package domain.action.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.action.contracts.IActionRequest;
import domain.location.Location;

public abstract class ActionRequest implements IActionRequest
{
	protected final List<Location> locations = new ArrayList<Location>();
	
	public ActionRequest(Location... locations)
	{
		for(Location location : locations)
		{
			addLocation(location);
		}
	}
	
	protected ActionRequest(Iterable<Location> locations)
	{
		for(Location location : locations)
		{
			addLocation(location);
		}
	}

	@Override
	public abstract boolean isCatch();

	@Override
	public List<Location> getLocations()
	{
		return Collections.unmodifiableList(locations);
	}
	
	@Override
	public Location getStart()
	{
		return getLocations().get(0);
	}
	
	@Override
	public Location getEnd()
	{
		return getLocations().get(getLocations().size()-1);
	}
	
	protected void addLocation(Location location)
	{
		locations.add(location);
	}
	
	@Override
	public abstract int getNumberOfCatches();
	
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
		if(obj instanceof ActionRequest)
		{
			IActionRequest casted = (IActionRequest)obj;
			return 	this.isCatch() == casted.isCatch() &&
					this.getLocations().equals(casted.getLocations());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(isCatch()) + 37 * locations.hashCode();
	}
}
