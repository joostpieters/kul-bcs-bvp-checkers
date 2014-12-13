package domain.board;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class BoardSizeTests {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testGetRows() {
		BoardSize x = new BoardSize(2, 4);
		assertEquals(2, x.getRows());
	}

	@Test
	public void testGetCols() {
		BoardSize x = new BoardSize(2, 4);
		assertEquals(4, x.getCols());
	}

	@Test
	public void testBoardSizeValid() {
		BoardSize x = new BoardSize(100, 200);
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
		BoardSize x = new BoardSize(12, 12);
		int max = 12*12/2;
		assertTrue(x.isValidIndex(1));
		assertTrue(x.isValidIndex(12));
		assertTrue(x.isValidIndex(max));
	}
	
	@Test
	public void testIsInvalidIndex() {
		BoardSize x = new BoardSize(12, 12);
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
		BoardSize x = new BoardSize(6, 8);
		assertTrue(x.isValidLocation(0, 0));
		assertTrue(x.isValidLocation(5, 7));
	}
	
	@Test
	public void testIsValidLocationOutBoundsFalse() {
		BoardSize x = new BoardSize(4, 2);
		assertFalse(x.isValidLocation(-1, 0));
		assertFalse(x.isValidLocation(0, -1));
		assertFalse(x.isValidLocation(4, 1));
		assertFalse(x.isValidLocation(3, 2));
	}

	@Test
	public void testEqualsSameSize() {
		BoardSize x = new BoardSize(2,2);
		BoardSize y = new BoardSize(2,2);
		assertTrue(x.equals(y));
		assertTrue(y.equals(x));
	}
	
	@Test
	public void testEqualsOtherRowSize() {
		BoardSize x = new BoardSize(2,2);
		BoardSize y = new BoardSize(4,2);
		assertFalse(x.equals(y));
		assertFalse(y.equals(x));
	}
	
	@Test
	public void testEqualsOtherColSize() {
		BoardSize x = new BoardSize(2,2);
		BoardSize y = new BoardSize(2,4);
		assertFalse(x.equals(y));
		assertFalse(y.equals(x));
	}
	
	@Test
	public void testEqualsNull() {
		BoardSize x = new BoardSize(2,2);
		assertFalse(x.equals(null));
	}
	
	@Test
	public void testEqualsObject() {
		BoardSize x = new BoardSize(2,2);
		assertFalse(x.equals(new Object()));
	}
}
