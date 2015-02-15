package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public class ActionRequestPriorityComparatorTest
{
	private final static ActionRequestPriorityComparator comparator = new ActionRequestPriorityComparator();
	private final static IBoardSize size = new BoardSize(10, 10);
	
	@Test
	public void testCompareEqual() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);	
		ActionRequest request1 = new CatchActionRequest(Player.White, a, b, c);
		ActionRequest request2 = new CatchActionRequest(Player.White, a, b, c);
		
		assertEquals(0, comparator.compare(request1, request2));
	}
	
	@Test
	public void testCompareMore() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		ActionRequest request1 = new CatchActionRequest(Player.White, a, b, c);
		ActionRequest request2 = new CatchActionRequest(Player.White, a, b);
		
		assertEquals(1, comparator.compare(request1, request2));
	}
	
	@Test
	public void testCompareLess() throws LocationOutOfRangeException
	{
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		Location c = new Location(3, size);
		ActionRequest request1 = new CatchActionRequest(Player.White, a, b);
		ActionRequest request2 = new CatchActionRequest(Player.White, a, b, c);
		
		assertEquals(-1, comparator.compare(request1, request2));
	}
}
