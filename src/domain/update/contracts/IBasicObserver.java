package domain.update.contracts;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;

/**
 * Implementers can observe the {@link IGame} by receiving {@link IBoard} and status updates.
 * As such, the methods below are typically dispatched by an external source. 
 * Instances typically subscribe to classes that implement {@link IBasicUpdateSource}.
 */
public interface IBasicObserver
{
	/**
	 * Indicates that the Board has been updated.
	 *  
	 * @param 	board
	 * 			The board that has been updated.
	 * @param	performer
	 * 			The Player that initiated the Action that caused this update.
	 */
	public void updateBoard(IReadOnlyBoard board, Player performer);

	/**
	 * Indicates that it is now the given Player's turn.
	 * 
	 * @param	board
	 * 			The current Baord.
	 * @param	switchedIn
	 * 			The newly active Player.
	 */
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn);
	
	/**
	 * Indicates that the given Action has been executed.
	 * 
	 * @param 	action
	 * 			The action that has been executed. 
	 */
	public void executeAction(IAction action);
}
