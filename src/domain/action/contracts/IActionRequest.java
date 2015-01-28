package domain.action.contracts;

import java.util.List;

import domain.board.contracts.IBoardSize;
import domain.location.Location;

public interface IActionRequest
{
	public boolean isCatch();

	public List<Integer> getIndices();

	public int getStartIndex();

	public int getEndIndex();

	public Location getStart(IBoardSize size);

	public Location getEnd(IBoardSize size);

	public int getNumberOfCatches();
}