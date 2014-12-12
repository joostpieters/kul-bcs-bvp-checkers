package ui;

import domain.IGameFollower;
import domain.board.Board;

public interface IVisualizer extends IGameFollower {
	public void paint(Board board);
	public void close();
}
