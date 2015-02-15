package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

import domain.action.contracts.IActionRequest;
import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public class MoveActionRequestTest
{
	private final static IBoardSize size = new BoardSize(10, 10);
	
	@Test
	public void testIsCatch() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		IActionRequest request = new MoveActionRequest(Player.White, a, b);
		assertFalse(request.isCatch());
	}

	@Test
	public void testGetNumberOfCatches() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		IActionRequest request = new MoveActionRequest(Player.White, a, b);
		assertEquals(0, request.getNumberOfCatches());
	}
}
