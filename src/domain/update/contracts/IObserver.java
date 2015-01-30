package domain.update.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

/**
 * Implementers can observe the {@link IGame} by receiving {@link IBoard} and status updates.
 * As such, the methods below are typically dispatched by an external source. 
 * Instances typically subscribe to classes that implement {@link IUpdateSource}.
 */
public interface IObserver extends IBasicObserver
{	
	/**
	 * Indicates that a promotable Piece has reached its promotion row.
	 * 
	 * @param	board
	 * 			The current Board after promotion.
	 * @param 	location
	 * 			The location of the Piece to be promoted.
	 */
	public void firePromotion(IReadOnlyBoard board, Location location);
	
	/**
	 * Indicates that the Game is over.
	 * 
	 * @param 	winner
	 * 			The winner of the Game. Can be null in case of remise.
	 */
	public void fireGameOver(Player winner);
	
	/**
	 * Indicates that the given player is out of moves.
	 *  
	 * @param 	player
	 * 			The Player who cannot perform any more valid Action.
	 */
	public void fireOutOfMoves(Player player);
	
	/**
	 * Indicates that given Player proposes remise.
	 * 
	 * @param 	proposer
	 * 			The Player that proposes remise.
	 */
	public void fireProposeRemise(Player proposer);
	
	/**
	 * Indicates that both Players agreed on remise.
	 */
	public void fireAcceptRemise();
	
	/**
	 * Indicates that no agreement on remise was reached.
	 */
	public void fireDeclineRemise();
	
	/**
	 * Indicates that a forced remise took place.
	 */
	public void fireForcedRemise();
	
	/**
	 * Indicates that the given Player has resigned.
	 * 
	 * @param 	resignee
	 * 			The Player who resigned.
	 */
	public void fireResign(Player resignee);
	
	/**
	 * Indicates that the Game has started.
	 * 
	 * @param 	board
	 * 			The initial Board.
	 * @param 	starter
	 * 			The Player that plays first.
	 */
	public void fireStart(IReadOnlyBoard board, Player starter);
	
	/**
	 * Indicates that something has happened that requires the Players to be notified.
	 * 
	 * @param 	message
	 * 			The message to show to the Players.
	 */
	public void fireWarning(String message);
	
	/**
	 * Indicates that something serious has happened that requires the Players to be notified.
	 *  
	 * @param 	message
	 * 			The message to show to the Players.
	 * @param 	ex
	 * 			The accompanying Exception.
	 */
	public void fireError(String message, Exception ex);
}
