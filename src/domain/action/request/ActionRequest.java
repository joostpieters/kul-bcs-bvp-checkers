package domain.action.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.board.BoardSize;
import domain.location.Location;

public abstract class ActionRequest
{
	protected final List<Integer> indices = new ArrayList<Integer>();
	
	public ActionRequest(int... indices) {
		for(int index : indices)
		{
			addIndex(index);
		}
	}
	
	protected ActionRequest(Iterable<Integer> indices)
	{
		for(int index : indices)
		{
			addIndex(index);
		}
	}

	public abstract boolean isCatch();

	public List<Integer> getIndices() {
		return Collections.unmodifiableList(indices);
	}
	
	public int getStartIndex()
	{
		return getIndices().get(0);
	}
	
	public int getEndIndex()
	{
		return getIndices().get(getIndices().size()-1);
	}
	
	public Location getStart(BoardSize size)
	{
		return new Location(getStartIndex(), size);
	}
	
	public Location getEnd(BoardSize size)
	{
		return new Location(getEndIndex(), size);
	}
	
	protected void addIndex(int index)
	{
		indices.add(index);
	}
	
	public abstract int getNumberOfCatches();
	
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
			return 	this.isCatch() == casted.isCatch() &&
					this.getIndices().equals(casted.getIndices());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(isCatch()) ^ indices.hashCode();
	}
}
