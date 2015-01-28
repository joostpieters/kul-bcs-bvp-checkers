package domain.action.request;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import domain.action.contracts.IActionRequest;
import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;

public class ActionRequestTest
{

	@Test
	public void testActionRequestIntArray()
	{
		IActionRequest request = new CatchActionRequest(1,2,3);
		List<Integer> indices = request.getIndices();
		assertTrue(indices.contains(1));
		assertTrue(indices.contains(2));
		assertTrue(indices.contains(3));
	}

	@Test
	public void testActionRequestIterableOfInteger()
	{
		CatchActionRequest request1 = new CatchActionRequest(1, 2);
		CatchActionRequest request2 = new AtomicCatchActionRequest(2, 3);
		CatchActionRequest request = new CatchActionRequest(request1, request2);
		List<Integer> indices = request.getIndices();
		assertTrue(indices.contains(1));
		assertTrue(indices.contains(2));
		assertTrue(indices.contains(3));
	}

	@Test
	public void testGetStartIndex()
	{
		IActionRequest request = new CatchActionRequest(1,2,3);
		assertEquals(1, request.getStartIndex());
	}

	@Test
	public void testGetEndIndex()
	{
		IActionRequest request = new CatchActionRequest(1,2,3);
		assertEquals(3, request.getEndIndex());
	}

	@Test
	public void testGetStart()
	{
		IBoardSize size = new BoardSize(10, 10);
		IActionRequest request = new CatchActionRequest(1,2,3);
		assertEquals(new Location(1, size), request.getStart(size));
	}

	@Test
	public void testGetEnd()
	{
		IBoardSize size = new BoardSize(10, 10);
		IActionRequest request = new CatchActionRequest(1,2,3);
		assertEquals(new Location(3, size), request.getEnd(size));
	}

	@Test
	public void testAddIndex()
	{
		ActionRequest request = new CatchActionRequest(1,2);
		request.addIndex(3);
		assertEquals(3, request.getEndIndex());
	}

	@Test
	public void testEqualsObject()
	{
		IActionRequest request = new CatchActionRequest(1,2);
		IActionRequest copy = new CatchActionRequest(1, 2);
		IActionRequest other = new CatchActionRequest(1, 2, 3);
		IActionRequest moveRequest = new MoveActionRequest(1, 2);
		assertFalse(request.equals(null));
		assertFalse(request.equals(new Object()));
		assertTrue(request.equals(request));
		assertTrue(request.equals(copy));
		assertFalse(request.equals(other));
		assertFalse(request.equals(moveRequest));
	}
}
