package domain.updates.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;

/**
 * This interface indicates that the implementer can observe the Game by receiving Board updates. 
 * Instances typically subscribe to classes that implement {@link IBasicGameUpdateSource}.
 * This is the Observer in the Observer pattern   
 */
public interface IBasicGameObserver
{
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the Board has updated.
	 *  
	 * @param 	board
	 * 			The board that has been updated.
	 * @param	performer
	 * 			The Player that initiated the Action that caused this update.
	 */
	public void updateBoard(IReadOnlyBoard board, Player performer);
}
