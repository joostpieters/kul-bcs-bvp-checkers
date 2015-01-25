package domain.board.contracts;

public interface IBoardSize
{

	public int getRows();

	public int getCols();

	public boolean isValidIndex(int index);

	public boolean isValidLocation(int row, int col);

}