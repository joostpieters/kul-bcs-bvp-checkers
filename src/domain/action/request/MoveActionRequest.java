package domain.action.request;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.location.Location;

/**
 * Represents a specific {@link IActionRequest} specifically related to moves.
 */
public class MoveActionRequest extends ActionRequest
{
	/**
	 * Creates a new {@link MoveActionRequest} with the given parameters.
	 * @param 	player
	 * 			The {@link Player} that made this {@link ActionRequest}.
	 * @param 	from
	 * 			The from {@link Location} in this {@link ActionRequest}.
	 * @param 	to
	 * 			The to {@link Location} in this {@link ActionRequest}.
	 */
	public MoveActionRequest(Player player, Location from, Location to)
	{
		super(player, from, to);
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
