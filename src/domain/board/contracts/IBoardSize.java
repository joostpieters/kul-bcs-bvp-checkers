package domain.board.contracts;

import domain.location.Location;

/**
 * Represents the size of {@link IBoard}s.
 */
public interface IBoardSize
{
	/**
	 * Returns the number of rows of this {@link IBoardSize}.
	 */
	public int getRows();

	/**
	 * Returns the number of columns of this {@link IBoardSize}.
	 */
	public int getCols();

	/**
	 * Returns true if the given (1-based) location index falls somewhere in this {@link IBoardSize},
	 * false otherwise.
	 */
	public boolean isValidIndex(int index);

	/**
	 * Returns true if the given {@link Location} falls somewhere in this {@link IBoardSize},
	 * false otherwise.
	 */
	public boolean isValidLocation(int row, int col);

}