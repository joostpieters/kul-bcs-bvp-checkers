package domain.updates.contracts;

/**
 * Represents an update to be sent to one or more {@link IGameObserver}s. 
 */
public interface IUpdate
{
	/**
	 * Send this update to the given observer.
	 */
	public void sendTo(IGameObserver observer);
}
