package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.action.contracts.IActionRequest;

public class MoveActionRequestTest
{
	@Test
	public void testIsCatch()
	{
		IActionRequest request = new MoveActionRequest(1,2);
		assertFalse(request.isCatch());
	}

	@Test
	public void testGetNumberOfCatches()
	{
		IActionRequest request = new MoveActionRequest(1,2);
		assertEquals(0, request.getNumberOfCatches());
	}
}
