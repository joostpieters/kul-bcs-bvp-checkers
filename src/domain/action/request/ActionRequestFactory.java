package domain.action.request;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import ui.LocalizationManager;

/**
 * A Factory that creates {@link IActionRequest}s based on textual input.
 */
public class ActionRequestFactory
{
	private final IBoardSize size;
	
	private IBoardSize getSize()
	{
		return size;
	}
	
	/**
	 * Create a new {@link ActionRequestFactory} for {@link IBoard}s of the given size.
	 * @param 	size
	 * 			The size of {@link IBoard}s the created {@link IActionRequest} are made for.
	 */
	public ActionRequestFactory(IBoardSize size)
	{
		this.size = size;
	}
	
	/**
	 * Create a new {@link IActionRequest} for the given {@link Player} based on the given textual input.
	 * @param 	player
	 * 			The {@link Player} that made the request.
	 * @param 	move
	 * 			A textual representation of the request.
	 * @throws LocationOutOfRangeException
	 */
	public IActionRequest create(Player player, String move) throws LocationOutOfRangeException
	{
		if(move.matches("\\d+\\s*-\\s*\\d+")) //step or fly
		{
			String parts[] = move.split("\\s*-\\s*");
			int fromIndex = Integer.parseInt(parts[0]);
			int toIndex = Integer.parseInt(parts[1]);
			
			Location from = new Location(fromIndex, getSize());
			Location to = new Location(toIndex, getSize());
			return new MoveActionRequest(player, from, to);
		}
		else if(move.matches("\\d+(\\s*x\\s*\\d+)+")) //(multi-)(fly-)catch
		{
			String[] parts = move.split("\\s*x\\s*");
			Location[] locations = new Location[parts.length];
			for(int i=0; i < parts.length; i++)
			{
				int index = Integer.parseInt(parts[i]);
				locations[i] = new Location(index, getSize());
			}
			return new CatchActionRequest(player, locations);
		}
		throw new IllegalArgumentException(LocalizationManager.getString("invalidPatternException"));
	}
}
