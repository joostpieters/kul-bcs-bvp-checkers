package domain.action.request;

import domain.action.contracts.IActionRequest;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import ui.LocalizationManager;

public class ActionRequestFactory
{
	private final IBoardSize size;
	
	private IBoardSize getSize()
	{
		return size;
	}
	
	public ActionRequestFactory(IBoardSize size)
	{
		this.size = size;
	}
	
	public IActionRequest create(String move) throws LocationOutOfRangeException
	{
		if(move.matches("\\d+\\s*-\\s*\\d+")) //step or fly
		{
			String parts[] = move.split("\\s*-\\s*");
			int fromIndex = Integer.parseInt(parts[0]);
			int toIndex = Integer.parseInt(parts[1]);
			
			Location from = new Location(fromIndex, getSize());
			Location to = new Location(toIndex, getSize());
			return new MoveActionRequest(from, to);
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
			return new CatchActionRequest(locations);
		}
		throw new IllegalArgumentException(LocalizationManager.getString("invalidPatternException"));
	}
}
