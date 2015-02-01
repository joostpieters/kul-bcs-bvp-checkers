package domain.location;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import common.Player;
import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;
import domain.location.Location;

public class LocationTest
{
	private Location loc1x1on4x4;
	private Location loc4x5on8x8;
	private Location loc9x0on10x10;
	private Location loc0x0on10x10;
	private Location loc4x5on8x10;
	private Location loc4x5on10x8;
	
	@Before
	public void setup()
	{
		try
		{
			loc1x1on4x4 = new Location(1, 1, new BoardSize(4, 4));
			loc4x5on8x8 = new Location(4, 5, new BoardSize(8, 8));
			loc9x0on10x10 = new Location(9, 0, new BoardSize(10, 10));
			loc0x0on10x10 = new Location(0, 0, new BoardSize(10, 10));
			loc4x5on8x10 = new Location(4, 5, new BoardSize(8, 10));
			loc4x5on10x8 = new Location(4, 5, new BoardSize(10, 8));
		}
		catch (LocationOutOfRangeException e)
		{
			assert false;
		}
	}
	
	@Test(expected=LocationOutOfRangeException.class)
	public void testJustOutOfRange() throws LocationOutOfRangeException
	{
		new Location(8,8, new BoardSize(8, 8));
	}
	
	@Test(expected=LocationOutOfRangeException.class)
	public void testMoreOutOfRange() throws LocationOutOfRangeException
	{
		new Location(20,20, new BoardSize(8, 8));
	}
	
	@Test(expected=LocationOutOfRangeException.class)
	public void testNegativeRowOutOfRange() throws LocationOutOfRangeException
	{
		new Location(-1,5, new BoardSize(8, 8));
	}
	
	@Test(expected=LocationOutOfRangeException.class)
	public void testNegativeColOutOfRange() throws LocationOutOfRangeException
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
	public void testGetIndexFailsOnWhite()
	{
		loc1x1on4x4.getIndex();
	}

	@Test
	public void testFromIndex() throws LocationOutOfRangeException
	{
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
	
	@Test(expected=LocationOutOfRangeException.class)
	public void testFromIndexTooHigh() throws LocationOutOfRangeException
	{
		new Location(10, new BoardSize(4, 4));
	}

	@Test(expected=LocationOutOfRangeException.class)
	public void testFromIndexTooLow() throws LocationOutOfRangeException
	{
		new Location(0, new BoardSize(4, 4));
	}
	
	@Test
	public void testLocationFromLocation()
	{
		Location loc = new Location(loc4x5on8x8);
		assertFalse(loc == loc4x5on8x8);
		assertEquals(loc4x5on8x8, loc);
	}
	
	@Test
	public void testSuccessiveIndices() throws LocationOutOfRangeException
	{
		int rows = 2 * (1 + (int)(Math.random() * 25));
		int cols = 2 * (1 + (int)(Math.random() * 25));
		IBoardSize size = new BoardSize(rows, cols);
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
	
	@Test
	public void testEqualsFalse() throws LocationOutOfRangeException
	{
		assertFalse(loc4x5on8x8.equals(null));
		assertFalse(loc4x5on8x8.equals(new Object()));
		assertFalse(loc4x5on8x8.equals(loc1x1on4x4));
		assertFalse(loc4x5on8x8.equals(new Location(4,6,loc4x5on8x8.getBoardSize())));
		assertFalse(loc4x5on8x8.equals(new Location(4,5,new BoardSize(10, 10))));
	}
	
	@Test
	public void testEqualsTrue()
	{
		assertTrue(loc4x5on8x8.equals(loc4x5on8x8));
		assertTrue(loc4x5on8x8.equals(new Location(loc4x5on8x8)));
	}
	
	@Test
	public void testOnPromotionRow()
	{
		assertTrue(loc9x0on10x10.isOnPromotionRow(Player.Black));
		assertFalse(loc9x0on10x10.isOnPromotionRow(Player.White));
		assertTrue(loc0x0on10x10.isOnPromotionRow(Player.White));
		assertFalse(loc0x0on10x10.isOnPromotionRow(Player.Black));
	}
	
	@Test
	public void testSameBoardSize()
	{
		assertTrue(loc0x0on10x10.equalBoardSize(loc9x0on10x10));
		assertFalse(loc1x1on4x4.equalBoardSize(loc4x5on8x8));
		assertFalse(loc4x5on10x8.equalBoardSize(loc4x5on8x10));
	}
	
	@Test
	public void testDirections() throws LocationOutOfRangeException
	{
		assertTrue(loc4x5on8x8.isAbove(loc4x5on8x8.getBelow()));
		assertTrue(loc4x5on8x8.isBelow(loc4x5on8x8.getAbove()));
		assertTrue(loc4x5on8x8.isLeftFrom(loc4x5on8x8.getRight()));
		assertTrue(loc4x5on8x8.isRightFrom(loc4x5on8x8.getLeft()));
		
		assertTrue(loc4x5on8x8.isInFront(loc4x5on8x8.getBack(Player.White), Player.White));
		assertTrue(loc4x5on8x8.isInFront(loc4x5on8x8.getBack(Player.Black), Player.Black));
		assertTrue(loc4x5on8x8.isBehind(loc4x5on8x8.getFront(Player.White), Player.White));
		assertTrue(loc4x5on8x8.isBehind(loc4x5on8x8.getFront(Player.Black), Player.Black));
		
		assertFalse(loc4x5on8x8.isAbove(loc4x5on8x8.getAbove()));
		assertFalse(loc4x5on8x8.isBelow(loc4x5on8x8.getBelow()));
		assertFalse(loc4x5on8x8.isLeftFrom(loc4x5on8x8.getLeft()));
		assertFalse(loc4x5on8x8.isRightFrom(loc4x5on8x8.getRight()));
		
		assertFalse(loc4x5on8x8.isInFront(loc4x5on8x8.getFront(Player.White), Player.White));
		assertFalse(loc4x5on8x8.isInFront(loc4x5on8x8.getFront(Player.Black), Player.Black));
		assertFalse(loc4x5on8x8.isBehind(loc4x5on8x8.getBack(Player.White), Player.White));
		assertFalse(loc4x5on8x8.isBehind(loc4x5on8x8.getBack(Player.Black), Player.Black));
	}
	
	@Test
	public void testRelativeLocation() throws LocationOutOfRangeException
	{
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Above).isAbove(loc1x1on4x4));
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Below).isBelow(loc1x1on4x4));
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Left).isLeftFrom(loc1x1on4x4));
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Right).isRightFrom(loc1x1on4x4));
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Front).isInFront(loc1x1on4x4, Player.White));
		assertTrue(loc1x1on4x4.getRelativeLocation(Player.White, Direction.Back).isBehind(loc1x1on4x4, Player.White));
	}
}
