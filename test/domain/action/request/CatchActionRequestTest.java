package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

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
		CatchActionRequest request = new CatchActionRequest(a, b);
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
		CatchActionRequest request = new CatchActionRequest(a, b, c, d, e);
		assertEquals(4, request.getNumberOfCatches());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCatchActionRequestCatchActionRequestCatchActionRequest() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		Location d = new Location(4, size);
		CatchActionRequest request1 = new CatchActionRequest(a, b);
		CatchActionRequest request2 = new CatchActionRequest(c, d);
		new CatchActionRequest(request1, request2);
	}

}
