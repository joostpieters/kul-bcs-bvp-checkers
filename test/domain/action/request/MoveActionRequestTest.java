package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoveActionRequestTest
{
	@Test
	public void testIsCatch()
	{
		ActionRequest request = new MoveActionRequest(1,2);
		assertFalse(request.isCatch());
	}

	@Test
	public void testGetNumberOfCatches()
	{
		ActionRequest request = new MoveActionRequest(1,2);
		assertEquals(0, request.getNumberOfCatches());
	}
}
