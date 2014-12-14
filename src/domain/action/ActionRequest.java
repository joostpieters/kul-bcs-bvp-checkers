package domain.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionRequest
{
	private final boolean isCatch;
	private final List<Integer> indices = new ArrayList<Integer>();
	
	public ActionRequest(boolean isCatch, int... indices) {
		this.isCatch = isCatch;
		for(int index : indices)
		{
			addIndex(index);
		}
	}

	public boolean isCatch() {
		return isCatch;
	}
	
	public void addIndex(int index)
	{
		indices.add(index);
	}

	public List<Integer> getIndices() {
		return Collections.unmodifiableList(indices);
	}
	
	public int getNumberOfCatches()
	{
		return isCatch() ? getIndices().size() - 1 : 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
		{
			return false;
		}
		if(this == obj) //same reference
		{
			return true;
		}
		if(obj instanceof ActionRequest)
		{
			ActionRequest casted = (ActionRequest)obj;
			return 	this.isCatch == casted.isCatch &&
					this.indices.equals(casted.indices);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(isCatch) ^ indices.hashCode();
	}
	
	@Override
	public String toString() {
		if(isCatch)
		{
			return String.join("x", getIndices().stream().map(i -> i.toString()).collect(Collectors.toList())); 
		}
		else
		{
			int fromIndex = getIndices().get(0);
			int toIndex = getIndices().get(1);
			return String.format("%d-%d", fromIndex, toIndex);
		}
	}
}
