package ui;

import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IGameObserver;

public interface IVisualizer extends IGameObserver
{
	public void paint(IReadOnlyBoard board);
}
