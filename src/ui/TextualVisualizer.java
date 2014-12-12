package ui;

import domain.board.Board;

public class TextualVisualizer implements IVisualizer {

	@Override
	public void paint(Board board) {
		System.out.println(board);
	}

	@Override
	public void close() {
		//do nothing
	}

	@Override
	public void update(Board board) {
		paint(board);
	}

}
