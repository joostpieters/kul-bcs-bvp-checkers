package domain.action.contracts;

import java.util.List;

import common.Player;
import domain.action.request.ActionRequest;
import domain.location.Location;

/**
 * A request for {@link IAction}. It contains a list of {@link Location} indices and a method to differentiate between move and catch requests. 
 */
public interface IActionRequest
{
	/**
	 * Returns true if this {@link IAction} performs a catch,
	 * false otherwise.
	 */
	public boolean isCatch();

	/**
	 * Returns the {@link Player} that requested this {@link ActionRequest}.
	 */
	public Player getPlayer();
	
	/**
	 * Returns the {@link List} of indices in this {@link IActionRequest}.
	 */
	public List<Location> getLocations();

	/**
	 * Returns the Location corresponding to the first index.
	 */
	public Location getStart();

	/**
	 * Returns the Location corresponding to the last index.
	 */
	public Location getEnd();

	/**
	 * Returns the number of catches. The result lies between 0 and #indices-1.
	 */
	public int getNumberOfCatches();
}