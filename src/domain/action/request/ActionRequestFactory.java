package domain.action.request;

import ui.LocalizationManager;

public class ActionRequestFactory
{
	private ActionRequestFactory() 	{}
	
	public static ActionRequest create(String move)
	{
		if(move.matches("\\d+\\s*-\\s*\\d+")) //step or fly
		{
			String parts[] = move.split("\\s*-\\s*");
			int fromIndex = Integer.parseInt(parts[0]);
			int toIndex = Integer.parseInt(parts[1]);
			return new MoveActionRequest(fromIndex, toIndex);
		}
		else if(move.matches("\\d+(\\s*x\\s*\\d+)+")) //(multi-)(fly-)catch
		{
			String[] parts = move.split("\\s*x\\s*");
			if(parts.length == 2)
			{
				int fromIndex = Integer.parseInt(parts[0]);
				int toIndex = Integer.parseInt(parts[1]);
				return new AtomicCatchActionRequest(fromIndex, toIndex);
			}
			else
			{
				int[] indices = new int[parts.length];
				for(int i=0; i < parts.length; i++)
				{
					indices[i] = Integer.parseInt(parts[i]);
				}
				return new CatchActionRequest(indices);
			}
		}
		throw new IllegalArgumentException(LocalizationManager.getString("invalidPatternException"));
	}
}
