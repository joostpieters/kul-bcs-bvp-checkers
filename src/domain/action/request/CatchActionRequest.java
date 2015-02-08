package domain.action.request;

import java.util.List;
import java.util.stream.Collectors;

import domain.location.Location;

public class CatchActionRequest extends ActionRequest
{
	public CatchActionRequest(Location... locations)
	{
		super(locations);
	}
	
	public CatchActionRequest(CatchActionRequest base, CatchActionRequest addendum)
	{
		super(base.getLocations());
		Location lastBaseLocation = base.getEnd();
		Location firstExtraLocation = addendum.getStart();
		if(!lastBaseLocation.equals(firstExtraLocation))
		{
			throw new IllegalArgumentException("Given addendum cannot be chained onto base.");
		}
		List<Location> locationsToAppend = addendum.getLocations();
		for(int i=1; i < locationsToAppend.size(); i++) //skip firstExtraIndex
		{
			Location location = locationsToAppend.get(i);
			addLocation(location);
		}
	}
	
	@Override
	public String toString()
	{
		return String.join("x", getLocations().stream().map(i -> Integer.toString(i.getIndex())).collect(Collectors.toList())); 
	}

	@Override
	public boolean isCatch()
	{
		return true;
	}

	@Override
	public int getNumberOfCatches()
	{
		return getLocations().size() - 1;
	}
}
