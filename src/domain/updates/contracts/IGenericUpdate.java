package domain.updates.contracts;

/**
 * Represents an update to be sent to one or more Observers.
 * 
 *  @param 	<T>
 *  		The kind of Observer this Updates is tailored to.
 */
public interface IGenericUpdate<T extends IBasicObserver>
{
	/**
	 * Send this update to the given observer.
	 * 
	 * @param	observer
	 * 			The observer to send the update to.
	 */
	public void sendTo(T observer);
}
