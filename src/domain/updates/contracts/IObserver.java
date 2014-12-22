package domain.updates.contracts;

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
	public void promotion(IReadOnlyBoard board, Location location);
	
	/**
	 * Indicates that the Game is over.
	 * 
	 * @param 	winner
	 * 			The winner of the Game. Can be null in case of remise.
	 */
	public void gameOver(Player winner);
	
	/**
	 * Indicates that the given player is out of moves.
	 *  
	 * @param 	player
	 * 			The Player who cannot perform any more valid Action.
	 */
	public void outOfMoves(Player player);
	
	/**
	 * Indicates that given Player proposes remise.
	 * 
	 * @param 	proposer
	 * 			The Player that proposes remise.
	 */
	public void proposeRemise(Player proposer);
	
	/**
	 * Indicates that both Players agreed on remise.
	 */
	public void acceptRemise();
	
	/**
	 * Indicates that no agreement on remise was reached.
	 */
	public void declineRemise();
	
	/**
	 * Indicates that a forced remise took place.
	 */
	public void forcedRemise();
	
	/**
	 * Indicates that the given Player has resigned.
	 * 
	 * @param 	resignee
	 * 			The Player who resigned.
	 */
	public void resign(Player resignee);
	
	/**
	 * Indicates that the Game has started.
	 * 
	 * @param 	board
	 * 			The initial Board.
	 * @param 	starter
	 * 			The Player that plays first.
	 */
	public void start(IReadOnlyBoard board, Player starter);
	
	/**
	 * Indicates that something has happened that requires the Players to be notified.
	 * 
	 * @param 	message
	 * 			The message to show to the Players.
	 */
	public void warning(String message);
	
	/**
	 * Indicates that something serious has happened that requires the Players to be notified.
	 *  
	 * @param 	message
	 * 			The message to show to the Players.
	 * @param 	ex
	 * 			The accompanying Exception.
	 */
	public void error(String message, Exception ex);
}
