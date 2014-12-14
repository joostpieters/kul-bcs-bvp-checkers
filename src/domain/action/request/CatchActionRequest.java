package domain.action.request;

import java.util.stream.Collectors;

public class CatchActionRequest extends ActionRequest
{
	public CatchActionRequest(int... indices)
	{
		super(indices);
	}
	
	public CatchActionRequest(CatchActionRequest base, CatchActionRequest addendum)
	{
		super(base.getIndices());
		int lastBaseIndex = base.getEndIndex();
		int firstExtraIndex = addendum.getStartIndex();
		if(lastBaseIndex != firstExtraIndex)
		{
			throw new IllegalArgumentException("Given addendum cannot be chained onto base.");
		}
		for(int index : addendum.getIndices())
		{
			addIndex(index);
		}
	}
	
	@Override
	public String toString() {
		return String.join("x", getIndices().stream().map(i -> i.toString()).collect(Collectors.toList())); 
	}

	@Override
	public boolean isCatch() {
		return true;
	}

	@Override
	public int getNumberOfCatches() {
		return getIndices().size() - 1;
	}
}
