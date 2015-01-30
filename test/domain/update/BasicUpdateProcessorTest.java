package domain.update;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.observer.OutOfMovesObserver;
import domain.observer.PromotionObserver;
import domain.update.BasicUpdateProcessor;

public class BasicUpdateProcessorTest
{
	private static BasicUpdateProcessor processor;
	private static BasicUpdateProcessor processor2;
	
	@Before
	public void setup()
	{
		processor = new PromotionObserver();
		processor2 = new OutOfMovesObserver();
	}
	
	@Test
	public void testLinkUnlinkBasic()
	{
		assertFalse(processor.isLinkedBasic(processor2));
		assertFalse(processor2.isLinkedBasic(processor));
		assertFalse(processor.isSubscribedBasic(processor2));
		assertFalse(processor2.isSubscribedBasic(processor));
		processor.linkBasic(processor2);
		assertTrue(processor.isLinkedBasic(processor2));
		assertTrue(processor2.isLinkedBasic(processor));
		assertTrue(processor.isSubscribedBasic(processor2));
		assertTrue(processor2.isSubscribedBasic(processor));
		processor.unlinkBasic(processor2);
		assertFalse(processor.isLinkedBasic(processor2));
		assertFalse(processor2.isLinkedBasic(processor));
		assertFalse(processor.isSubscribedBasic(processor2));
		assertFalse(processor2.isSubscribedBasic(processor));
	}

	@Test //(expected=IllegalStateException.class)
	public void testUnlinkBasicNotLinked()
	{
		processor.unlinkBasic(processor2);
	}

	@Test //(expected=IllegalStateException.class)
	public void testLinkAlreadyLinked()
	{
		processor.linkBasic(processor2);
		processor.linkBasic(processor2);
	}
	
	@Test
	public void testIsLinkedPartialLeft()
	{
		processor.subscribeBasic(processor2);
		assertFalse(processor.isLinkedBasic(processor2));
	}
	
	@Test
	public void testIsLinkedPartialRight()
	{
		processor2.subscribeBasic(processor);
		assertFalse(processor.isLinkedBasic(processor2));
	}
}
