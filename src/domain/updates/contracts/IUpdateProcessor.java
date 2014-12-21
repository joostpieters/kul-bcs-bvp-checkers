package domain.updates.contracts;

/**
 * Combines {@link IUpdateSource} with {@link IObserver} to form an {@link IUpdateProcessor}.
 * This interface is typically employed to directly propagate any updates it receives from its members to its subscribers.
 * However, any instance that observes updates, processes them and emits them again can use this interface.
 */
public interface IUpdateProcessor extends IObserver, IUpdateSource, IBasicUpdateProcessor
{
	/**
	 * Subscribe the given processor to updates of this instance.
	 * Also subscribe this instance to updates of the given processor.
	 * 
	 * @param 	processor
	 * 			The {@link IUpdateProcessor} to link with.
	 */
	public void subscribeBothWays(IUpdateProcessor processor);
}
