package domain.update.contracts;

/**
 * Combines {@link IUpdateSource} with {@link IObserver} to form an {@link IUpdateProcessor}.
 * This interface is typically employed to directly propagate any updates it receives from its observees to its observers.
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
	public void link(IUpdateProcessor processor);
	
	/**
	 * Unsubscribe the given processor to updates of this instance.
	 * Also unsubscribe this instance to updates of the given processor.
	 * 
	 * @param 	processor
	 * 			The {@link IUpdateProcessor} to unlink with.
	 */
	public void unlink(IUpdateProcessor processor);
	
	/**
	 * Returns true if the given processor is linked to this instance, false otherwise.
	 * 
	 * @param 	processor
	 * 			The {@link IUpdateProcessor} to check.
	 * @return TODO
	 */
	public boolean isLinked(IUpdateProcessor processor);
}
