package domain.updates.contracts;

/**
 * Provides a basic interface for every class that wants to accept {@link IGameFollower}s and send them updates.  
 */
public interface IGameUpdateSource {

	/**
	 * Subscribe a follower to receive future updates about the game.
	 * 
	 * @param 	follower
	 * 			The follower that will receive updates.
	 */
	public abstract void subscribe(IGameFollower follower);

	/**
	 * Unsubscribe a follow to stop receiving updates about the game.
	 * 
	 * @param 	follower
	 * 			The follower to unsubscribe.
	 */
	public abstract void unsubscribe(IGameFollower follower);

}