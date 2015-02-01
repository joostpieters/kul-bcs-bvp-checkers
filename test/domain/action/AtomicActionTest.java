package domain.action;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;
import domain.location.LocationOutOfRangeException;

public class AtomicActionTest
{
	@Test
	public void testAtomicAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		
		assertEquals(pair, action.getPair());
	}

	@Test
	public void testGetFrom() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		
		assertEquals(pair.getFrom(), action.getFrom());
	}
	
	@Test
	public void testEquals() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pairStep = new DiagonalLocationPair(1, 7, size);
		DiagonalLocationPair pairStep2 = new DiagonalLocationPair(7, 12, size);
		DiagonalLocationPair pairCatch = new DiagonalLocationPair(1, 12, size);
		AtomicAction stepAction = new AtomicActionStep(pairStep);
		AtomicAction stepAction2 = new AtomicActionStep(pairStep2);
		AtomicAction catchAction = new AtomicActionCatch(pairCatch);
		
		assertFalse(stepAction.equals(null));
		assertFalse(stepAction.equals(new Object()));
		assertTrue(stepAction.equals(stepAction));
		assertFalse(stepAction.equals(stepAction2));
		assertFalse(stepAction.equals(catchAction));
	}
}
