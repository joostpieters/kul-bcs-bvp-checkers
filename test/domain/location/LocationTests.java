package domain.location;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.board.BoardSize;
import domain.location.Location;

public class LocationTests {
	private final Location loc1x1on4x4 = new Location(1, 1, new BoardSize(4, 4));
	private final Location loc4x5on8x8 = new Location(4, 5, new BoardSize(8, 8));
	private final Location loc9x0on10x10 = new Location(9, 0, new BoardSize(10, 10));
	
	@Test(expected=IllegalArgumentException.class)
	public void testJustOutOfRange()
	{
		new Location(8,8, new BoardSize(8, 8));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMoreOutOfRange()
	{
		new Location(20,20, new BoardSize(8, 8));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeRowOutOfRange()
	{
		new Location(-1,5, new BoardSize(8, 8));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeColOutOfRange()
	{
		new Location(5,-1, new BoardSize(8, 8));
	}
	
	@Test
	public void testIsWhite() {
		assertTrue(loc1x1on4x4.isWhite());
		assertFalse(loc4x5on8x8.isWhite());
		assertFalse(loc9x0on10x10.isWhite());
	}

	@Test
	public void testIsBlack() {
		assertFalse(loc1x1on4x4.isBlack());
		assertTrue(loc4x5on8x8.isBlack());
		assertTrue(loc9x0on10x10.isBlack());
	}

	@Test
	public void testGetIndex() {
		assertEquals(19, loc4x5on8x8.getIndex());
		assertEquals(46, loc9x0on10x10.getIndex());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetIndexFailsOnWhite() {
		loc1x1on4x4.getIndex();
	}

	@Test
	public void testFromIndex() {
		Location idx19on8x8 = new Location(19, new BoardSize(8, 8));
		assertEquals(4, idx19on8x8.getRow());
		assertEquals(5, idx19on8x8.getCol());
		
		Location idx46on10x10 = new Location(46, new BoardSize(10, 10));
		assertEquals(9, idx46on10x10.getRow());
		assertEquals(0, idx46on10x10.getCol());
		
		Location idx3on3x3 = new Location(3, new BoardSize(4, 4));
		assertEquals(1, idx3on3x3.getRow());
		assertEquals(0, idx3on3x3.getCol());
		
		Location idx2on2x3 = new Location(2, new BoardSize(2, 4));
		assertEquals(0, idx2on2x3.getRow());
		assertEquals(3, idx2on2x3.getCol());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromIndexTooHigh() {
		new Location(5, new BoardSize(3, 3));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromIndexTooLow() {
		new Location(0, new BoardSize(3, 3));
	}
	
	@Test
	public void testSuccessiveIndices()
	{
		int rows = 2 * (int)(Math.random() * 25);
		int cols = 2 * (int)(Math.random() * 25);
		BoardSize size = new BoardSize(rows, cols);
		int prevIndex = 1;
		for(int row=0; row<rows; row++)
		{
			for(int col=0; col<cols; col++)
			{
				Location loc = new Location(row, col, size);
				if(loc.isBlack())
				{
					int index = loc.getIndex();
					assertEquals(prevIndex++, index);
					
					Location recalculated = new Location(index, size);
					assertEquals(row, recalculated.getRow());
					assertEquals(col, recalculated.getCol());
				}
			}
		}
	}
}
