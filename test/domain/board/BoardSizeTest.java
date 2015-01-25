package domain.board;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.board.contracts.IBoardSize;


public class BoardSizeTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testGetRows() {
		IBoardSize x = new BoardSize(2, 4);
		assertEquals(2, x.getRows());
	}

	@Test
	public void testGetCols() {
		IBoardSize x = new BoardSize(2, 4);
		assertEquals(4, x.getCols());
	}

	@Test
	public void testBoardSizeValid() {
		IBoardSize x = new BoardSize(100, 200);
		assertEquals(100, x.getRows());
		assertEquals(200, x.getCols());
	}
	
	@Test
	public void testBoardSizeInvalid() {
		exception.expect(IllegalArgumentException.class);
	    exception.expectMessage("Invalid board size (9, 9).");
		new BoardSize(9, 9);
	}

	@Test
	public void testIsValidIndex() {
		IBoardSize x = new BoardSize(12, 12);
		int max = 12*12/2;
		assertTrue(x.isValidIndex(1));
		assertTrue(x.isValidIndex(12));
		assertTrue(x.isValidIndex(max));
	}
	
	@Test
	public void testIsInvalidIndex() {
		IBoardSize x = new BoardSize(12, 12);
		int max = 12*12/2;
		assertFalse(x.isValidIndex(-1));
		assertFalse(x.isValidIndex(0));
		assertFalse(x.isValidIndex(max+1));
	}

	@Test
	public void testIsValidSize() {
		assertTrue(BoardSize.isValidSize(8, 8));
		assertTrue(BoardSize.isValidSize(10, 10));
	}
	
	@Test
	public void testIsValidSizeUnevenRows()
	{
		boolean valid = BoardSize.isValidSize(9, 10);
		assertFalse(valid);
	}
	
	@Test
	public void testIsValidSizeUnevenCols()
	{
		boolean valid = BoardSize.isValidSize(8, 9);
		assertFalse(valid);
	}
	
	@Test
	public void testIsValidSizeZeroRows()
	{
		boolean valid = BoardSize.isValidSize(0, 2);
		assertFalse(valid);
	}
	
	@Test
	public void testIsValidSizeZeroCols()
	{
		boolean valid = BoardSize.isValidSize(6, 0);
		assertFalse(valid);
	}

	@Test
	public void testIsValidLocationInBoundsTrue() {
		IBoardSize x = new BoardSize(6, 8);
		assertTrue(x.isValidLocation(0, 0));
		assertTrue(x.isValidLocation(5, 7));
	}
	
	@Test
	public void testIsValidLocationOutBoundsFalse() {
		IBoardSize x = new BoardSize(4, 2);
		assertFalse(x.isValidLocation(-1, 0));
		assertFalse(x.isValidLocation(0, -1));
		assertFalse(x.isValidLocation(4, 1));
		assertFalse(x.isValidLocation(3, 2));
	}

	@Test
	public void testEqualsSameSize() {
		IBoardSize x = new BoardSize(2,2);
		IBoardSize y = new BoardSize(2,2);
		assertTrue(x.equals(y));
		assertTrue(y.equals(x));
	}
	
	@Test
	public void testEqualsOtherRowSize() {
		IBoardSize x = new BoardSize(2,2);
		IBoardSize y = new BoardSize(4,2);
		assertFalse(x.equals(y));
		assertFalse(y.equals(x));
	}
	
	@Test
	public void testEqualsOtherColSize() {
		IBoardSize x = new BoardSize(2,2);
		IBoardSize y = new BoardSize(2,4);
		assertFalse(x.equals(y));
		assertFalse(y.equals(x));
	}
	
	@Test
	public void testEqualsNull() {
		IBoardSize x = new BoardSize(2,2);
		assertFalse(x.equals(null));
	}
	
	@Test
	public void testEqualsObject() {
		IBoardSize x = new BoardSize(2,2);
		assertFalse(x.equals(new Object()));
	}
}
