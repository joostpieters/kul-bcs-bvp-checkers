package domain.location;

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
}