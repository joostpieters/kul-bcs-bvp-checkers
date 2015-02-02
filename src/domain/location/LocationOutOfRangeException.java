package domain.location;

import domain.board.contracts.IBoardSize;

/**
 * An {@link Exception} that is typically thrown when the player attempts to reference a 
 * {@link Location} (either by row and column or by index) that would fall outside the {@link IBoardSize}.
 */
public class LocationOutOfRangeException extends Exception
{
	private static final long serialVersionUID = 3691225736394551268L;

	public LocationOutOfRangeException()
	{
		super();
	}

	public LocationOutOfRangeException(String message)
	{
		super(message);
	}
}
