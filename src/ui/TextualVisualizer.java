package ui;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;

public class TextualVisualizer implements IVisualizer {

	@Override
	public void paint(IReadOnlyBoard board) {
		System.out.println(board);
	}

	@Override
	public void close() {
		//do nothing
	}

	@Override
	public void update(IReadOnlyBoard board) {
		paint(board);
	}

	@Override
	public void gameOver(Player winner) {
		close();		
	}

}
