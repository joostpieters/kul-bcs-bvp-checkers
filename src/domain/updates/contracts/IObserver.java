package domain.updates.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

/**
 * This interface indicates that the implementer can observe the Game by receiving Board and status updates.
 * As such, the methods below are typically dispatched by an external source. 
 * Instances typically subscribe to classes that implement {@link IUpdateSource}.
 */
public interface IObserver extends IBasicObserver
{	
	/**
	 * This method indicates that a promotable Piece has reached its promotion row.
	 * 
	 * @param	board
	 * 			The current Board after promotion.
	 * @param 	location
	 * 			The location of the Piece to be promoted.
	 */
	public void promotion(IReadOnlyBoard board, Location location);
	
	/**
	 * This method indicates that the Game is over.
	 * 
	 * @param 	winner
	 * 			The winner of the Game. Can be null in case of remise.
	 */
	public void gameOver(Player winner);
	
	/**
	 * This method indicates that the given player is out of moves.
	 *  
	 * @param 	player
	 * 			The Player who cannot perform any more valid Action.
	 */
	public void outOfMoves(Player player);
	
	/**
	 * This method indicates that given Player proposes remise.
	 * 
	 * @param 	proposer
	 * 			The Player that proposes remise.
	 */
	public void proposeRemise(Player proposer);
	
	/**
	 * This method indicates that both Players agreed on remise.
	 */
	public void acceptRemise();
	
	/**
	 * This method indicates that no agreement on remise was reached.
	 */
	public void declineRemise();
	
	/**
	 * This method indicates that a forced remise took place.
	 */
	public void forcedRemise();
	
	/**
	 * This method indicates that the given Player has resigned.
	 * 
	 * @param 	resignee
	 * 			The Player who resigned.
	 */
	public void resign(Player resignee);
	
	/**
	 * This method indicates that the Game has started.
	 * 
	 * @param 	board
	 * 			The initial Board.
	 * @param 	starter
	 * 			The Player that plays first.
	 */
	public void start(IReadOnlyBoard board, Player starter);
	
	/**
	 * This method indicates that something has happened that requires the Players to be notified.
	 * 
	 * @param 	message
	 * 			The message to show to the Players.
	 */
	public void warning(String message);
	
	/**
	 * This method indicates that something serious has happened that requires the Players to be notified.
	 *  
	 * @param 	message
	 * 			The message to show to the Players.
	 * @param 	ex
	 * 			The accompanying Exception.
	 */
	public void error(String message, Exception ex);
}
