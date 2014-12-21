package domain.updates.contracts;

/**
 * Represents an update to be sent to one or more {@link IBasicObserver}s. 
 */
public interface IBasicUpdate
{
	/**
	 * Send this update to the given observer.
	 */
	public void sendTo(IBasicObserver observer);
}
