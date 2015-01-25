package domain.location;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DirectionTest
{

	@Test
	public void testReverse()
	{
		assertEquals(Direction.Above, Direction.Below.reverse());
		assertEquals(Direction.Below, Direction.Above.reverse());
		assertEquals(Direction.Left, Direction.Right.reverse());
		assertEquals(Direction.Right, Direction.Left.reverse());
		assertEquals(Direction.Front, Direction.Back.reverse());
		assertEquals(Direction.Back, Direction.Front.reverse());
	}
	
	@Test
	public void testDoubleReverse()
	{
		for(Direction dir : Direction.values())
		{
			assertEquals(dir, dir.reverse().reverse());
		}
	}
	
	@Test
	public void testDiagonalDirections()
	{
		List<Direction[]> diagonals = Direction.getDiagonalDirections();
		for(Direction[] diagonal : diagonals)
		{
			assertTrue(diagonal.length == 2);
			assertNotEquals(diagonal[0], diagonal[1]);
			assertNotEquals(diagonal[0], diagonal[1].reverse());
		}
	}
}
