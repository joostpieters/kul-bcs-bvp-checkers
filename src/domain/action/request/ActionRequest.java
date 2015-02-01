package domain.action.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.action.contracts.IActionRequest;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

public abstract class ActionRequest implements IActionRequest
{
	protected final List<Integer> indices = new ArrayList<Integer>();
	
	public ActionRequest(int... indices)
	{
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

	@Override
	public abstract boolean isCatch();

	@Override
	public List<Integer> getIndices()
	{
		return Collections.unmodifiableList(indices);
	}
	
	@Override
	public int getStartIndex()
	{
		return getIndices().get(0);
	}
	
	@Override
	public int getEndIndex()
	{
		return getIndices().get(getIndices().size()-1);
	}
	
	@Override
	public Location getStart(IBoardSize size) throws LocationOutOfRangeException
	{
		return new Location(getStartIndex(), size);
	}
	
	@Override
	public Location getEnd(IBoardSize size) throws LocationOutOfRangeException
	{
		return new Location(getEndIndex(), size);
	}
	
	protected void addIndex(int index)
	{
		indices.add(index);
	}
	
	@Override
	public abstract int getNumberOfCatches();
	
	@Override
	public boolean equals(Object obj)
	{
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
			IActionRequest casted = (IActionRequest)obj;
			return 	this.isCatch() == casted.isCatch() &&
					this.getIndices().equals(casted.getIndices());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(isCatch()) + 37 * indices.hashCode();
	}
}
