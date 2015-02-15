package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public class CatchActionRequestTest
{
	private final static IBoardSize size = new BoardSize(10, 10);
	
	@Test
	public void testIsCatch() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		CatchActionRequest request = new CatchActionRequest(Player.White, a, b);
		assertTrue(request.isCatch());
	}

	@Test
	public void testGetNumberOfCatches() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		Location d = new Location(4, size);
		Location e = new Location(5, size);
		CatchActionRequest request = new CatchActionRequest(Player.White, a, b, c, d, e);
		assertEquals(4, request.getNumberOfCatches());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCatchActionRequestChainingFailEndPoints() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		Location d = new Location(4, size);
		CatchActionRequest request1 = new CatchActionRequest(Player.White, a, b);
		CatchActionRequest request2 = new CatchActionRequest(Player.White, c, d);
		new CatchActionRequest(request1, request2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCatchActionRequestChainingFailPlayers() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		CatchActionRequest request1 = new CatchActionRequest(Player.White, a, b);
		CatchActionRequest request2 = new CatchActionRequest(Player.Black, b, c);
		new CatchActionRequest(request1, request2);
	}
	
	@Test
	public void testCatchActionRequestChainingSuccess() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		CatchActionRequest request1 = new CatchActionRequest(Player.White, a, b);
		CatchActionRequest request2 = new CatchActionRequest(Player.White, b, c);
		CatchActionRequest chained = new CatchActionRequest(request1, request2);
		assertEquals(a, chained.getStart());
		assertEquals(c, chained.getEnd());
	}

}
