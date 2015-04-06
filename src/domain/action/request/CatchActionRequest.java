package domain.action.request;

import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.location.Location;

/**
 * Represents a specific {@link IActionRequest} specifically related to catches.
 */
public class CatchActionRequest extends ActionRequest
{
	/**
	 * Creates a new {@link CatchActionRequest} with the given parameters.
	 * @param 	player
	 * 			The {@link Player} that made this {@link ActionRequest}.
	 * @param 	locations
	 * 			The {@link Location}s in this {@link ActionRequest}.
	 */
	public CatchActionRequest(Player player, Location... locations)
	{
		super(player, locations);
	}
	
	/**
	 * Creates a new {@link CatchActionRequest} based on two other {@link CatchActionRequest}s: a base and an addemdum.
	 * @param 	base
	 * 			This request will form the base of the new request.
	 * @param 	addendum
	 * 			The {@link Location}s in this request will be appended to the base request to form a new request.
	 */
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
