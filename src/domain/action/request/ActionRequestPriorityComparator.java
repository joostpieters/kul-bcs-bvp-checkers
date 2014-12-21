package domain.action.request;

import java.util.Comparator;

/**
 * This comparator imposes a priority-based ordering on {@link ActionRequest}s.
 * An Action's priority correlates directly with the amount of catches therein.
 * 
 * Note: this priority-based ordering does not constitute a natural ordering and is inconsistent with equals.
 */
public class ActionRequestPriorityComparator implements Comparator<ActionRequest>
{
	@Override
	public int compare(ActionRequest x, ActionRequest y)
	{
		int catchesInX = x.getNumberOfCatches();
		int catchesInY = y.getNumberOfCatches();
		
		return Integer.compare(catchesInX, catchesInY);
	}
}
