package domain.update;

import domain.update.contracts.IBasicUpdateProcessor;

/**
 * A convenient, basic implementation of {@link IBasicUpdateProcessor}.
 * 
 * Note that this class extends {@link UpdateSource} instead of {@link BasicUpdateSource}. 
 * This allows the class to only subscribe to basic events, but still emit all events.
 */
public abstract class BasicUpdateProcessor extends UpdateSource implements IBasicUpdateProcessor
{
	@Override
	public void linkBasic(IBasicUpdateProcessor processor)
	{
		this.subscribeBasic(processor);
		processor.subscribeBasic(this);
	}
	
	@Override
	public void unlinkBasic(IBasicUpdateProcessor processor)
	{
		this.unsubscribeBasic(processor);
		processor.unsubscribeBasic(this);
	}
	
	@Override
	public boolean isLinkedBasic(IBasicUpdateProcessor processor)
	{
		return this.isSubscribedBasic(processor) && processor.isSubscribedBasic(this);
	}
}
