package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.action.contracts.IActionRequest;
import domain.board.BoardSize;
import domain.location.LocationOutOfRangeException;

public class ActionRequestFactoryTest
{
	private final ActionRequestFactory factory = new ActionRequestFactory(new BoardSize(10, 10));
	@Test
	public void testStep() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create("1-2");
		assertFalse(request.isCatch());
	}

	@Test
	public void testCatch() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create("1x2");
		assertTrue(request.isCatch());
		assertEquals(1, request.getNumberOfCatches());
	}
	
	@Test
	public void testMutiCatch() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create("1x2x3");
		assertTrue(request.isCatch());
		assertEquals(2, request.getNumberOfCatches());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPattern() throws LocationOutOfRangeException
	{
		factory.create("lalala");
	}
}
