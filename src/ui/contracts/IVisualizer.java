package ui.contracts;

import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IObserver;

public interface IVisualizer extends IObserver
{
	public void paint(IReadOnlyBoard board);
}
