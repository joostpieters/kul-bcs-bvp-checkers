package domain.updates.contracts;

/**
 * Provides a basic interface for every class that wants to accept {@link IGameObserver}s and send them updates.
 * This is the Subject in the Observer pattern.  
 */
public interface IGameUpdateSource extends IBasicGameUpdateSource
{
	/**
	 * Subscribe an observer to receive future updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer that will receive updates.
	 */
	public void subscribe(IGameObserver observer);

	/**
	 * Unsubscribe an observer to stop receiving updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer to unsubscribe.
	 */
	public void unsubscribe(IGameObserver observer);
}