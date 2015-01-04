package domain.updates.contracts;

/**
 * Combines {@link IBasicUpdateSource} with {@link IBasicObserver} to form an {@link IBasicUpdateProcessor}.
 * This interface is typically employed to directly propagate any updates it receives from its observees to its observers.
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
	public void linkBasic(IBasicUpdateProcessor processor);
	
	/**
	 * Unsubscribe the given processor to updates of this instance.
	 * Also unsubscribe this instance to updates of the given processor.
	 * 
	 * @param 	processor
	 * 			The {@link IBasicUpdateProcessor} to unlink with.
	 */
	public void unlinkBasic(IBasicUpdateProcessor processor);
	
	/**
	 * Returns true if the given processor is linked to this instance, false otherwise.
	 * 
	 * @param 	processor
	 * 			The {@link IBasicUpdateProcessor} to check.
	 * @return TODO
	 */
	public boolean isLinkedBasic(IBasicUpdateProcessor processor);
}
