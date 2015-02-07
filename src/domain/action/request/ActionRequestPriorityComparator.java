package domain.action.request;

import java.util.Comparator;

import domain.action.contracts.IActionRequest;

/**
 * This comparator imposes a priority-based ordering on {@link ActionRequest}s.
 * An Action's priority correlates directly with the amount of catches therein.
 * 
 * Note: this priority-based ordering does not constitute a natural ordering and is inconsistent with equals.
 */
public class ActionRequestPriorityComparator implements Comparator<IActionRequest>
{
	@Override
	public int compare(IActionRequest x, IActionRequest y)
	{
		int catchesInX = x.getNumberOfCatches();
		int catchesInY = y.getNumberOfCatches();
		
		return Integer.compare(catchesInX, catchesInY);
	}
}
