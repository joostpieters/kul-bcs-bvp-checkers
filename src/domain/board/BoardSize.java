package domain.board;

public class BoardSize {
	private int rows;
	private int cols;
	
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	public BoardSize(int rows, int cols) {
		if(!isValidSize(rows, cols))
		{
			throw new IllegalArgumentException(String.format("Invalid board size (%d, %d).", rows, cols));
		}
		this.rows = rows;
		this.cols = cols;
	}
	
	public boolean isValidIndex(int index)
	{
		return index >= 1 && index <= getRows()*getCols()/2;
	}
	
	public static boolean isValidSize(int rows, int cols)
	{
		return 	rows % 2 == 0 &&
				cols % 2 == 0;
	}
	
	public boolean isValidSquare(int row, int col)
	{
		return 	row >= 0 && row < getRows() &&
				col >= 0 && col < getCols();
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", getRows(), getCols());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
		{
			return false;
		}
		try
		{
			BoardSize casted = (BoardSize)obj;
			return 	this.getRows() == casted.getRows() && 
					this.getCols() == casted.getCols();
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}
}
