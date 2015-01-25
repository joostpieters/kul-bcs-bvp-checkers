package domain.action.request;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActionRequestFactoryTest
{
	@Test
	public void testStep()
	{
		ActionRequest request = ActionRequestFactory.create("1-2");
		assertFalse(request.isCatch());
	}

	@Test
	public void testCatch()
	{
		ActionRequest request = ActionRequestFactory.create("1x2");
		assertTrue(request.isCatch());
		assertEquals(1, request.getNumberOfCatches());
	}
	
	@Test
	public void testMutiCatch()
	{
		ActionRequest request = ActionRequestFactory.create("1x2x3");
		assertTrue(request.isCatch());
		assertEquals(2, request.getNumberOfCatches());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPattern()
	{
		ActionRequestFactory.create("lalala");
	}
}
