package ui.contracts;

import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.update.contracts.IObserver;

/**
 * Implementers of this interface can visualize the {@link IGame} as they see fit.
 */
public interface IVisualizer extends IObserver
{
	/**
	 * Visualizes the given board.
	 * @param 	board
	 * 			The board to visualize.
	 */
	public void paint(IReadOnlyBoard board);
}
