package domain.updates.contracts;

/**
 * Combines {@link IBasicUpdateSource} with {@link IBasicObserver} to form an {@link IBasicUpdateProcessor}.
 * This interface is typically employed to directly propagate any updates it receives from its members to its subscribers.
 * However, any instance that observes updates, processes them and emits them again can use this interface.
 */
public interface IBasicUpdateProcessor extends IBasicObserver, IBasicUpdateSource
{
	/**
	 * Subscribe the given processor to updates of this instance.
	 * Also subscribe this instance to updates of the given processor.
	 * 
	 * @param 	processor
	 * 			The {@link IBasicUpdateProcessor} to link with.
	 */
	public void subscribeBasicBothWays(IBasicUpdateProcessor propagator);
}
