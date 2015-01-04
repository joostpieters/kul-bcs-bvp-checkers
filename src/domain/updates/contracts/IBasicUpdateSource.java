package domain.updates.contracts;

/**
 * Provides a basic interface for every class that wants to accept {@link IBasicObserver}s and send them updates.
 */
public interface IBasicUpdateSource
{
	/**
	 * Subscribe an observer to receive future updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer that will receive updates.
	 */
	public void subscribeBasic(IBasicObserver observer);
	
	/**
	 * Unsubscribe an observer to stop receiving updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer to unsubscribe.
	 */
	public void unsubscribeBasic(IBasicObserver observer);
	
	/**
	 * Returns true if the given observer is already subscribed, false otherwise.
	 * 
	 * @param 	observer
	 * 			The observer to check
	 */
	public boolean isSubscribedBasic(IBasicObserver observer);
}
