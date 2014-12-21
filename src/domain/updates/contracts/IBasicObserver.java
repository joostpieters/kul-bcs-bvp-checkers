package domain.updates.contracts;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;

/**
 * This interface indicates that the implementer can observe the Game by receiving Board updates.
 * As such, the methods below are typically dispatched by an external source. 
 * Instances typically subscribe to classes that implement {@link IBasicUpdateSource}.
 */
public interface IBasicObserver
{
	/**
	 * This method indicates that the Board has updated.
	 *  
	 * @param 	board
	 * 			The board that has been updated.
	 * @param	performer
	 * 			The Player that initiated the Action that caused this update.
	 */
	public void updateBoard(IReadOnlyBoard board, Player performer);

	/**
	 * This method indicates that it is now the given Player's turn.
	 * 
	 * @param	board
	 * 			The current Baord.
	 * @param	switchedIn
	 * 			The active Player.
	 */
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn);
	
	/**
	 * This method indicates that the given Action has been executed.
	 * 
	 * @param 	action
	 * 			The action that has been executed. 
	 */
	public void executeAction(IAction action);
}
