package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class CatchActionRequestTest
{

	@Test
	public void testIsCatch()
	{
		CatchActionRequest request = new CatchActionRequest(1, 2);
		assertTrue(request.isCatch());
	}

	@Test
	public void testGetNumberOfCatches()
	{
		CatchActionRequest request = new CatchActionRequest(1, 2, 3, 4, 5);
		assertEquals(4, request.getNumberOfCatches());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCatchActionRequestCatchActionRequestCatchActionRequest()
	{
		CatchActionRequest request1 = new CatchActionRequest(1, 2);
		CatchActionRequest request2 = new CatchActionRequest(3, 4);
		new CatchActionRequest(request1, request2);
	}

}
