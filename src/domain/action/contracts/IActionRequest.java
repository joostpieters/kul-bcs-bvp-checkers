package domain.action.contracts;

import java.util.List;

import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public interface IActionRequest
{
	/**
	 * Returns true if this {@link IAction} performs a catch,
	 * false otherwise.
	 */
	public boolean isCatch();

	/**
	 * Returns the {@link List} of indices in this {@link IActionRequest}.
	 */
	public List<Integer> getIndices();

	/**
	 * Returns the first index.
	 */
	public int getStartIndex();

	/**
	 * Returns the last index.
	 */
	public int getEndIndex();

	/**
	 * Returns the Location corresponding to the first index.
	 * 
	 * @throws 	LocationOutOfRangeException
	 * 			If the index is invalid on the given {@link IBoardSize}.
	 */
	public Location getStart(IBoardSize size) throws LocationOutOfRangeException;

	/**
	 * Returns the Location corresponding to the last index.
	 * 
	 * @throws 	LocationOutOfRangeException
	 * 			If the index is invalid on the given {@link IBoardSize}.
	 */
	public Location getEnd(IBoardSize size) throws LocationOutOfRangeException;

	/**
	 * Returns the number of catches. The result lies between 0 and #indices-1.
	 */
	public int getNumberOfCatches();
}