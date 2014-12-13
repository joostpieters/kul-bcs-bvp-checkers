package domain.updates.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;

/**
 * This interface indicates that the implementer can receive updates about the Game. 
 * This can be done by subscribing to classes that implement {@link IGameUpdateSource}.   
 */
public interface IGameFollower
{
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the Board has updated.
	 *  
	 * @param 	board
	 * 			The board that has been updated.
	 */
	public void update(IReadOnlyBoard board);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the Game is over.
	 * 
	 * @param 	winner
	 * 			The winner of the Game. Can be null in case of remise.
	 */
	public void gameOver(Player winner);
}
