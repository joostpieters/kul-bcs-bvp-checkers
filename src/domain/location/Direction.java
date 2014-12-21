package domain.location;

import java.util.ArrayList;
import java.util.List;

public enum Direction
{
	Above,
	Below,
	Front,
	Back,
	Left,
	Right;
	
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