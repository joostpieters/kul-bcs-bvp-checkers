package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActionRequestPriorityComparatorTest
{
	private final static ActionRequestPriorityComparator comparator = new ActionRequestPriorityComparator();
	
	@Test
	public void testCompareEqual()
	{
		ActionRequest request1 = new CatchActionRequest(1,2,3);
		ActionRequest request2 = new CatchActionRequest(1,2,3);
		
		assertEquals(0, comparator.compare(request1, request2));
	}
	
	@Test
	public void testCompareMore()
	{
		ActionRequest request1 = new CatchActionRequest(1,2,3);
		ActionRequest request2 = new CatchActionRequest(1,2);
		
		assertEquals(1, comparator.compare(request1, request2));
	}
	
	@Test
	public void testCompareLess()
	{
		ActionRequest request1 = new CatchActionRequest(1,2);
		ActionRequest request2 = new CatchActionRequest(1,2,3);
		
		assertEquals(-1, comparator.compare(request1, request2));
	}
}
