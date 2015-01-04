package domain.updates;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.observers.ForcedRemiseObserver;

public class UpdateProcessorTest
{
	private static UpdateProcessor processor;
	private static UpdateProcessor processor2;
	
	@Before
	public void setup()
	{
		processor = new ForcedRemiseObserver(10);
		processor2 = new ForcedRemiseObserver(20);
	}
	
	@Test
	public void testLinkUnlink()
	{
		assertFalse(processor.isLinked(processor2));
		assertFalse(processor2.isLinked(processor));
		assertFalse(processor.isSubscribed(processor2));
		assertFalse(processor2.isSubscribed(processor));
		processor.link(processor2);
		assertTrue(processor.isLinked(processor2));
		assertTrue(processor2.isLinked(processor));
		assertTrue(processor.isSubscribed(processor2));
		assertTrue(processor2.isSubscribed(processor));
		processor.unlink(processor2);
		assertFalse(processor.isLinked(processor2));
		assertFalse(processor2.isLinked(processor));
		assertFalse(processor.isSubscribed(processor2));
		assertFalse(processor2.isSubscribed(processor));
	}

	@Test(expected=IllegalStateException.class)
	public void testUnlinkNotLinked()
	{
		processor.unlink(processor2);
	}

	@Test(expected=IllegalStateException.class)
	public void testLinkAlreadyLinked()
	{
		processor.link(processor2);
		processor.link(processor2);
	}
	
	@Test
	public void testIsLinkedPartialLeft()
	{
		processor.subscribe(processor2);
		assertFalse(processor.isLinked(processor2));
	}
	
	@Test
	public void testIsLinkedPartialRight()
	{
		processor2.subscribe(processor);
		assertFalse(processor.isLinked(processor2));
	}
}
