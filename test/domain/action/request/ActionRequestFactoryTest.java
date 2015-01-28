package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.action.contracts.IActionRequest;

public class ActionRequestFactoryTest
{
	@Test
	public void testStep()
	{
		IActionRequest request = ActionRequestFactory.create("1-2");
		assertFalse(request.isCatch());
	}

	@Test
	public void testCatch()
	{
		IActionRequest request = ActionRequestFactory.create("1x2");
		assertTrue(request.isCatch());
		assertEquals(1, request.getNumberOfCatches());
	}
	
	@Test
	public void testMutiCatch()
	{
		IActionRequest request = ActionRequestFactory.create("1x2x3");
		assertTrue(request.isCatch());
		assertEquals(2, request.getNumberOfCatches());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPattern()
	{
		ActionRequestFactory.create("lalala");
	}
}
