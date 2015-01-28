package domain.update;

import domain.update.contracts.IUpdateProcessor;

/**
 * A convenient, basic implementation of {@link IUpdateProcessor}.
 */
public abstract class UpdateProcessor extends BasicUpdateProcessor implements IUpdateProcessor
{
	@Override
	public void link(IUpdateProcessor processor)
	{
		if(isLinked(processor))
		{
			throw new IllegalStateException("Given processor is already linked.");
		}
		this.subscribe(processor);
		processor.subscribe(this);
	}
	
	@Override
	public void unlink(IUpdateProcessor processor)
	{
		if(!isLinked(processor))
		{
			throw new IllegalStateException("Given processor is not linked to this instance.");
		}
		this.unsubscribe(processor);
		processor.unsubscribe(this);
	}
	
	@Override
	public boolean isLinked(IUpdateProcessor processor)
	{
		return this.isSubscribed(processor) && processor.isSubscribed(this);
	}
}
