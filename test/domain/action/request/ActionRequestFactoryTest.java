package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

import domain.action.contracts.IActionRequest;
import domain.board.BoardSize;
import domain.location.LocationOutOfRangeException;

public class ActionRequestFactoryTest
{
	private final ActionRequestFactory factory = new ActionRequestFactory(new BoardSize(10, 10));
	@Test
	public void testStep() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create(Player.White, "1-2");
		assertEquals(Player.White, request.getPlayer());
		assertFalse(request.isCatch());
	}

	@Test
	public void testCatch() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create(Player.Black, "1x2");
		assertEquals(Player.Black, request.getPlayer());
		assertTrue(request.isCatch());
		assertEquals(1, request.getNumberOfCatches());
	}
	
	@Test
	public void testMutiCatch() throws LocationOutOfRangeException
	{
		IActionRequest request = factory.create(Player.White, "1x2x3");
		assertEquals(Player.White, request.getPlayer());
		assertTrue(request.isCatch());
		assertEquals(2, request.getNumberOfCatches());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPattern() throws LocationOutOfRangeException
	{
		factory.create(Player.White, "lalala");
	}
}
