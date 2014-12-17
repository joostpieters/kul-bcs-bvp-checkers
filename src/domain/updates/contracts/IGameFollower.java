package domain.updates.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

/**
 * This interface indicates that the implementer can observe the Game by receiving Board and status updates. 
 * Instances typically subscribe to classes that implement {@link IGameUpdateSource}.
 * This is the Observer in the Observer pattern   
 */
public interface IGameFollower
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
	public void update(IReadOnlyBoard board, Player performer);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that a promotable Piece has reached its promotion row.
	 * 
	 * @param 	location
	 * 			The location of the Piece to be promoted.
	 */
	public void promotion(Location location);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the Game is over.
	 * 
	 * @param 	winner
	 * 			The winner of the Game. Can be null in case of remise.
	 */
	public void gameOver(Player winner);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the given player is out of moves.
	 *  
	 * @param 	player
	 * 			The Player who cannot perform any more valid Action.
	 */
	public void outOfMoves(Player player);
}
