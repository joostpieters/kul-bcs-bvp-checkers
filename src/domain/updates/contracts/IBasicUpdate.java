package domain.updates.contracts;

/**
 * Represents an update to be sent to one or more {@link IBasicGameObserver}s. 
 */
public interface IBasicUpdate
{
	/**
	 * Send this update to the given observer.
	 */
	public void sendTo(IBasicGameObserver observer);
}
