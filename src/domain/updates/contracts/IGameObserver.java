package domain.updates.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

/**
 * This interface indicates that the implementer can observe the Game by receiving Board and status updates. 
 * Instances typically subscribe to classes that implement {@link IGameUpdateSource}.
 * This is the Observer in the Observer pattern   
 */
public interface IGameObserver extends IBasicGameObserver
{	
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
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that given Player proposes remise.
	 * 
	 * @param 	proposer
	 * 			The Player that proposes remise.
	 */
	public void proposeRemise(Player proposer);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that both Players agreed on remise.
	 */
	public void agreeRemise();
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that no agreement on remise was reached.
	 */
	public void disagreeRemise();
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the given Player has resigned.
	 * 
	 * @param 	resignee
	 * 			The Player who resigned.
	 */
	public void resign(Player resignee);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that the Game has started.
	 * 
	 * @param 	board
	 * 			The initial Board.
	 * @param 	starter
	 * 			The Player that plays first.
	 */
	public void start(IReadOnlyBoard board, Player starter);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that something has happened that requires the Players to be notified.
	 * 
	 * @param 	message
	 * 			The message to show to the Players.
	 */
	public void warning(String message);
	
	/**
	 * This method is typically fired by an external source.
	 * It indicates that something serious has happened that requires the Players to be notified.
	 *  
	 * @param 	message
	 * 			The message to show to the Players.
	 * @param 	ex
	 * 			The accompanying Exception.
	 */
	public void error(String message, Exception ex);
}
