package ui.contracts;

import domain.board.contracts.IReadOnlyBoard;
import domain.update.contracts.IObserver;

public interface IVisualizer extends IObserver
{
	public void paint(IReadOnlyBoard board);
}
