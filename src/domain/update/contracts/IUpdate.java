package domain.update.contracts;

/**
 * Represents an update to be sent to one or more {@link IObserver}s. 
 */
public interface IUpdate extends IGenericUpdate<IObserver>
{
	/**
	 * {@inheritDoc}
	 */
	public void sendTo(IObserver observer);
}
