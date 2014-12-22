package domain.location;

import java.util.ArrayList;
import java.util.List;

/**
 * Specifies the basic {@link Direction}s on which to base relative navigation.
 * Four {@link Direction}s are semantically independent, but the other two
 * (Front, Back) require additional perspective information.
 */
public enum Direction
{
	Above,
	Below,
	Front,
	Back,
	Left,
	Right;
	
	/**
	 * Returns the reverse {@link Direction}.
	 */
	public Direction reverse()
	{
		switch(this)
		{
			case Above: return Below;
			case Below: return Above;
			case Front: return Back;
			case Back: return Front;
			case Left: return Right;
			case Right: return Left;
			default: throw new IllegalStateException("Unknown Direction: " + this);
		}
	}
	
	/**
	 * Returns the four basic diagonal {@link Direction}s by 
	 * combining Front and Back each with Left and Right. 
	 */
	public static List<Direction[]> getDiagonalDirections()
	{
		List<Direction[]> directions = new ArrayList<Direction[]>(4);
		directions.add(new Direction[] {Direction.Front, Direction.Right});
		directions.add(new Direction[] {Direction.Front, Direction.Left});
		directions.add(new Direction[] {Direction.Back, Direction.Right});
		directions.add(new Direction[] {Direction.Back, Direction.Left});
		return directions;
	}
}