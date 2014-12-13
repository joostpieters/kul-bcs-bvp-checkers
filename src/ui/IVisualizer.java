package ui;

import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IGameFollower;

public interface IVisualizer extends IGameFollower {
	public void paint(IReadOnlyBoard board);
	public void close();
}
