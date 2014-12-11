package ui;

import domain.board.Board;

public interface IVisualizer {
	public void paint(Board board);
	public void close();
}
