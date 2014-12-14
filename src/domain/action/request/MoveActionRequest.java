package domain.action.request;

public class MoveActionRequest extends ActionRequest
{
	public MoveActionRequest(int fromIndex, int toIndex)
	{
		super(fromIndex, toIndex);
	}
	
	@Override
	public String toString() {
		return String.format("%d-%d", getStartIndex(), getEndIndex());
	}

	@Override
	public boolean isCatch() {
		return false;
	}

	@Override
	public int getNumberOfCatches() {
		return 0;
	}
}
