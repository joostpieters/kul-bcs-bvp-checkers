package domain.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionFactory {
	private ActionFactory() { }
	
	public static Action create(String move) {
		if(ActionMove.isValidPattern(move))
		{
			Pattern movePattern = Pattern.compile(ActionMove.REGEX);
			Matcher matcher = movePattern.matcher(move);
			if(!matcher.matches())
			{
				throw new IllegalArgumentException("Invalid move pattern: " + move);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			return new ActionMove(fromIndex, toIndex);
		}
		if(ActionCatch.isValidPattern(move))
		{
			Pattern catchPattern = Pattern.compile(ActionCatch.REGEX);
			Matcher matcher = catchPattern.matcher(move);
			if(!matcher.matches())
			{
				throw new IllegalArgumentException("Invalid catch pattern: " + move);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			return new ActionCatch(fromIndex, toIndex);
		}
		if(move.matches("(\\d+)(\\s*x\\s*(\\d+))+")) //MultiCatch
		{			
			String[] parts = move.split("\\s*x\\s*");
			List<Integer> indices = new ArrayList<Integer>();
			Action[] actions = new Action[parts.length-1];
			for(String part : parts)
			{
				int index = Integer.parseInt(part);
				indices.add(index);
			}
			for(int i=0; i < parts.length - 1; i++)
			{
				actions[i] = new ActionCatch(indices.get(i), indices.get(i+1)); 
			}
			return new CompositeAction(actions);
		}
		throw new IllegalArgumentException("Invalid pattern");
	}
}
