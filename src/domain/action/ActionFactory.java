package domain.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.LocationPair;
import domain.board.BoardSize;

public class ActionFactory { //TODO refactor
	private ActionFactory() { }
	
	public static Action create(String move, BoardSize size) {
		if(AtomicActionStep.isValidPattern(move)) //step or fly
		{
			Pattern movePattern = Pattern.compile(AtomicActionStep.REGEX);
			Matcher matcher = movePattern.matcher(move);
			if(!matcher.matches())
			{
				throw new IllegalArgumentException("Invalid move pattern: " + move);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			LocationPair pair = new LocationPair(fromIndex, toIndex, size);
			
			if(pair.isOnSameDiagonal())
			{
				if(pair.getDiagonalDistance() == 1)
				{
					return new AtomicActionStep(pair);
				}
				else //dist > 1
				{
					return new ActionFly(pair);
				}
			}
			else
			{
				throw new IllegalArgumentException("Move pattern detected, but not on diagonal: " + move);
			}
		}
		else if(AtomicActionCatch.isValidPattern(move))
		{
			Pattern catchPattern = Pattern.compile(AtomicActionCatch.REGEX);
			Matcher matcher = catchPattern.matcher(move);
			if(!matcher.matches())
			{
				throw new IllegalArgumentException("Invalid catch pattern: " + move);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			LocationPair pair = new LocationPair(fromIndex, toIndex, size);
			
			return new AtomicActionCatch(pair);
		}
		else if(move.matches("(\\d+)(\\s*x\\s*(\\d+))+")) //MultiCatch
		{			
			String[] parts = move.split("\\s*x\\s*");
			Action[] actions = new Action[parts.length-1];
			for(int i=0; i < parts.length - 1; i++)
			{
				int fromIndex = Integer.parseInt(parts[i]);
				int toIndex = Integer.parseInt(parts[i+1]);
				LocationPair pair = new LocationPair(fromIndex, toIndex, size);
				actions[i] = new AtomicActionCatch(pair);
			}
			return new CompositeAction(actions);
		}
		throw new IllegalArgumentException("Invalid pattern");
	}
}
