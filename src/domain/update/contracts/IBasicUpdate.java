package domain.update.contracts;

/**
 * Represents an update to be sent to one or more {@link IBasicObserver}s. 
 */
public interface IBasicUpdate extends IGenericUpdate<IBasicObserver>
{
	/**
	 * {@inheritDoc}
	 */
	public void sendTo(IBasicObserver observer);
}
