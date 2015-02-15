package domain.action.request;

import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.location.Location;

public class CatchActionRequest extends ActionRequest
{
	public CatchActionRequest(Player player, Location... locations)
	{
		super(player, locations);
	}
	
	public CatchActionRequest(CatchActionRequest base, CatchActionRequest addendum)
	{
		super(base.getPlayer(), base.getLocations());
		if(base.getPlayer() != addendum.getPlayer())
		{
			throw new IllegalArgumentException("Both CatchActionRequests must originate from the same Player.");
		}
		Location lastBaseLocation = base.getEnd();
		Location firstExtraLocation = addendum.getStart();
		if(!lastBaseLocation.equals(firstExtraLocation))
		{
			throw new IllegalArgumentException("Cannot chain given CatchActionRequests: start/end does not match.");
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
