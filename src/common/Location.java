package common;

import domain.board.BoardSize;

public class Location
{
	private final int row;
	private final int col;
	private final BoardSize size;

	public Location(int row, int col, BoardSize size)
	{
		if(!size.isValidSquare(row, col))
		{
			throw new IllegalArgumentException(String.format("Location (%d, %d) is outside of the board size %s.", row, col, size));
		}
		this.row = row;
		this.col = col;
		this.size = size;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public BoardSize getBoardSize() {
		return size;
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", getRow(), getCol());
	}
	
	public boolean isWhite()
	{
		return (getRow() + getCol()) % 2 == 0;
	}
	
	public boolean isBlack()
	{
		return !isWhite();
	}
	
	public int getIndex()
	{
		if(isWhite())
		{
			throw new IllegalStateException(String.format("Location %s corresponds to a white square, which does not have an index.", this));
		}
		
		int squareNumber = getRow() * getBoardSize().getCols() + getCol(); //[0 - M*N[ 
		return squareNumber / 2 + 1; //only count black squares
	}
	
	public static Location fromIndex(int index, BoardSize size)
	{
		if(!size.isValidIndex(index))
		{
			throw new IllegalArgumentException(String.format("Invalid index %d for board size %s.", index, size));
		}
		index = index - 1; //zero-based indexing
		int row = index * 2 / size.getCols();
		int col = (index % (size.getCols() / 2)) * 2;
		if(row % 2 == 0)
		{
			col++;
		}
		
		return new Location(row, col, size);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
		{
			return false;
		}
		try
		{
			Location casted = (Location)obj;
			return 	this.getRow() == casted.getRow() && 
					this.getCol() == casted.getCol() &&
					this.getBoardSize().equals(casted.getBoardSize());
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}
	
	public boolean isPromotionRow(Player player)
	{
		return player == Player.Black ?
				getRow() == getBoardSize().getRows() - 1 :
				getRow() == 0;
	}
	
	public int getRowDistance(Location other)
	{
		return Math.abs(other.getRow()-getRow());
	}
	
	public int getColumnDistance(Location other)
	{
		return Math.abs(other.getCol()-getCol());
	}
	
	public boolean isOnSameDiagonal(Location other)
	{
		return getRowDistance(other) == getColumnDistance(other);
	}
	
	public int getDiagonalDistance(Location other)
	{
		if(!isOnSameDiagonal(other))
		{
			throw new IllegalArgumentException("Given location is not on same diagonal.");
		}
		
		return getRowDistance(other);
	}
	
	public boolean isInFrontOf(Location other, Player player)
	{
		int delta = getRow() - other.getRow(); //positive if this is lower than other on the board
		
		return (player == Player.White) ?
				delta < 0 :
				delta > 0;
	}
	
	public Location getCenterBetween(Location other)
	{
		if(	!isOnSameDiagonal(other) ||
			getDiagonalDistance(other) != 2)
		{
			throw new IllegalArgumentException("Cannot calculate center if other location is not on same diagonal or distance is not 2.");
		}
		int centerRow = (getRow() + other.getRow()) / 2;
		int centerCol = (getCol() + other.getCol()) / 2;
		return new Location(centerRow, centerCol, getBoardSize());
	}
}
