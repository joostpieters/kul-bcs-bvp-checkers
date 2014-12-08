package common;

public enum Player {
	Black,
	White;

	public Player getOpponent()
	{
		return this == Black ? White : Black;
	}
	
	@Override
	public String toString() {
		return "Player " + super.toString();
	}
}
