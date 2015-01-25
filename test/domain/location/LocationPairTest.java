package domain.location;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import common.Player;
import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;

public class LocationPairTest
{
	private final static IBoardSize size = new BoardSize(10, 10);
	private final static LocationPair stepAboveRight = new LocationPair(48, 43, size);
	private final static LocationPair stepAboveLeft = new LocationPair(38, 32, size);
	private final static LocationPair stepBelowRight = new LocationPair(18, 23, size);
	private final static LocationPair stepBelowLeft = new LocationPair(3, 8, size);
	private final static LocationPair flyFar = new LocationPair(46, 5, size);
	private final static LocationPair nonRestricted = new LocationPair(25, 44, size);
	
	@Test
	public void testGetFrom()
	{
		assertEquals(4, stepAboveRight.getFrom().getCol());
		assertEquals(9, stepAboveRight.getFrom().getRow());
	}
	
	@Test
	public void testGetTo()
	{
		assertEquals(5, stepAboveRight.getTo().getCol());
		assertEquals(8, stepAboveRight.getTo().getRow());
	}

	@Test(expected=NullPointerException.class)
	public void testConstructorFirstArgNull()
	{
		new LocationPair(null, new Location(0,0, size));
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorSecondArgNull()
	{
		new LocationPair(new Location(0,0, size), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorSameArgs()
	{
		Location loc = new Location(0,0, size);
		new LocationPair(loc, loc);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDifferentBoardSizes()
	{
		Location loc10x10 = new Location(0,0, size);
		Location loc8x8 = new Location(0,0, new BoardSize(8, 8));
		new LocationPair(loc10x10, loc8x8);
	}
	
	@Test
	public void testDistances()
	{
		assertEquals(1, stepAboveLeft.getRowDistance());
		assertEquals(1, stepAboveLeft.getColumnDistance());
	}
	
	@Test
	public void testSameDiagonal()
	{
		assertTrue(stepBelowRight.isOnSameDiagonal());
		assertTrue(stepBelowLeft.isOnSameDiagonal());
		assertTrue(stepAboveRight.isOnSameDiagonal());
		assertTrue(stepAboveLeft.isOnSameDiagonal());
		assertFalse(nonRestricted.isOnSameDiagonal());
	}
	
	@Test
	public void testSameRowAndCol()
	{
		LocationPair sameRow = new LocationPair(6, 10, size);
		LocationPair sameCol = new LocationPair(8, 38, size);
		
		assertTrue(sameRow.isOnSameRow());
		assertTrue(sameCol.isOnSameColumn());
		assertFalse(flyFar.isOnSameRow());
		assertFalse(nonRestricted.isOnSameColumn());
	}
	
	@Test
	public void testEqualsFalse()
	{
		assertFalse(stepAboveLeft.equals(null));
		assertFalse(stepAboveLeft.equals(new Object()));
		assertFalse(stepAboveLeft.equals(stepAboveRight));
		assertFalse(stepBelowLeft.equals(new LocationPair(3,9, size)));
		assertFalse(stepBelowLeft.equals(new Location(2,8, size)));
	}
	
	@Test
	public void testEqualsTrue()
	{
		assertTrue(flyFar.equals(flyFar));
		assertTrue(nonRestricted.equals(new LocationPair(nonRestricted)));
	}
	
	public void testGetUnitDirection(Direction... directions)
	{
		Location start = new Location(28, size);
		Location stop = start.getRelativeLocation(Player.White, directions);
		LocationPair pair = new LocationPair(start, stop);
		
		Collection<Direction> unitDirection = pair.getUnitDirection();
		for(Direction direction : directions)
		{
			//only true if directions don't cancel each other out
			assertTrue(unitDirection.contains(direction));			
		}
	}
	
	@Test
	public void testGetUnitDirection()
	{
		testGetUnitDirection(Direction.Above);
		testGetUnitDirection(Direction.Below);
		testGetUnitDirection(Direction.Left);
		testGetUnitDirection(Direction.Right);
		
		testGetUnitDirection(Direction.Above, Direction.Right);
		testGetUnitDirection(Direction.Above, Direction.Left);
		testGetUnitDirection(Direction.Below, Direction.Right);
		testGetUnitDirection(Direction.Below, Direction.Left);
		
		testGetUnitDirection(Direction.Below, Direction.Left, Direction.Below);
	}
}
