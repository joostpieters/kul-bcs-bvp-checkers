package domain.action.request;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import common.Player;

import domain.action.contracts.IActionRequest;
import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public class ActionRequestTest
{
	private final static IBoardSize size = new BoardSize(10, 10);
	@Test
	public void testActionRequestIntArray() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		IActionRequest request = new CatchActionRequest(Player.White, a, b, c);
		List<Location> indices = request.getLocations();
		assertTrue(indices.contains(a));
		assertTrue(indices.contains(b));
		assertTrue(indices.contains(c));
	}

	@Test
	public void testActionRequestIterableOfInteger() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		CatchActionRequest request1 = new CatchActionRequest(Player.White, a, b);
		CatchActionRequest request2 = new CatchActionRequest(Player.White, b, c);
		CatchActionRequest request = new CatchActionRequest(request1, request2);
		List<Location> indices = request.getLocations();
		assertTrue(indices.contains(a));
		assertTrue(indices.contains(b));
		assertTrue(indices.contains(c));
	}

	@Test
	public void testGetStart() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		IActionRequest request = new CatchActionRequest(Player.White, a, b, c);
		assertEquals(a, request.getStart());
	}

	@Test
	public void testGetEnd() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		IActionRequest request = new CatchActionRequest(Player.White, a, b, c);
		assertEquals(c, request.getEnd());
	}

	@Test
	public void testAddLocation() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		ActionRequest request = new CatchActionRequest(Player.White, a, b);
		request.addLocation(c);
		assertEquals(c, request.getEnd());
	}

	@Test
	public void testEquals() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		IActionRequest request = new CatchActionRequest(Player.White, a, b);
		IActionRequest copy = new CatchActionRequest(Player.White, a, b);
		IActionRequest other = new CatchActionRequest(Player.White, a, b, c);
		IActionRequest moveRequest = new MoveActionRequest(Player.White, a, b);
		assertFalse(request.equals(null));
		assertFalse(request.equals(new Object()));
		assertTrue(request.equals(request));
		assertTrue(request.equals(copy));
		assertFalse(request.equals(other));
		assertFalse(request.equals(moveRequest));
	}
}
