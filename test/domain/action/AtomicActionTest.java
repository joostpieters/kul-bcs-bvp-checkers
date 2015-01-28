package domain.action;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;

public class AtomicActionTest
{
	@Test
	public void testAtomicAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		
		assertEquals(pair, action.getPair());
	}

	@Test
	public void testGetFrom()
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		
		assertEquals(pair.getFrom(), action.getFrom());
	}
}
