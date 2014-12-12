package domain.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionRequest {
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
}
