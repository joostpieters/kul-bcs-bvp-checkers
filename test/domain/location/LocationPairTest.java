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
	private static LocationPair stepAboveRight;
	private static LocationPair stepAboveLeft;
	private static LocationPair stepBelowRight;
	private static LocationPair stepBelowLeft;
	private static LocationPair flyFar;
	private static LocationPair nonRestricted;
	
	static
	{
		try
		{
			stepAboveRight = new LocationPair(48, 43, size);
			stepAboveLeft = new LocationPair(38, 32, size);
			stepBelowRight = new LocationPair(18, 23, size);
			stepBelowLeft = new LocationPair(3, 8, size);
			flyFar = new LocationPair(46, 5, size);
			nonRestricted = new LocationPair(25, 44, size);
		}
		catch (LocationOutOfRangeException e)
		{
			assert false;
		}
	}
	
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
	public void testConstructorFirstArgNull() throws LocationOutOfRangeException
	{
		new LocationPair(null, new Location(0,0, size));
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorSecondArgNull() throws LocationOutOfRangeException
	{
		new LocationPair(new Location(0,0, size), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorSameArgs() throws LocationOutOfRangeException
	{
		Location loc = new Location(0,0, size);
		new LocationPair(loc, loc);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDifferentBoardSizes() throws LocationOutOfRangeException
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
	public void testSameRowAndCol() throws LocationOutOfRangeException
	{
		LocationPair sameRow = new LocationPair(6, 10, size);
		LocationPair sameCol = new LocationPair(8, 38, size);
		
		assertTrue(sameRow.isOnSameRow());
		assertTrue(sameCol.isOnSameColumn());
		assertFalse(flyFar.isOnSameRow());
		assertFalse(nonRestricted.isOnSameColumn());
	}
	
	@Test
	public void testEqualsFalse() throws LocationOutOfRangeException
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
	
	public void testGetUnitDirection(Direction... directions) throws LocationOutOfRangeException
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
	public void testGetUnitDirection() throws LocationOutOfRangeException
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
