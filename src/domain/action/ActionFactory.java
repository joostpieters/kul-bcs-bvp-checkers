package domain.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.RestrictedLocationPair;
import domain.board.BoardSize;

public class ActionFactory { //TODO refactor
	private ActionFactory() { }
	
	public static Action create(String move, BoardSize size) { //TODO domain only accepts typed actions/pairs, string parsing to ui?
		AnalysisResult analysis = analyzeMove(move);
		if(!analysis.isCatch())
		{
			List<Integer> indices = analysis.getIndices();
			assert(indices.size() == 2);
			int fromIndex = indices.get(0);
			int toIndex = indices.get(1);
			RestrictedLocationPair pair = new RestrictedLocationPair(fromIndex, toIndex, size);
			
			return createActionMove(pair);
		}
		else
		{
			List<Integer> indices = analysis.getIndices();
			if(indices.size() == 2)
			{
				int fromIndex = indices.get(0);
				int toIndex = indices.get(1);
				RestrictedLocationPair pair = new RestrictedLocationPair(fromIndex, toIndex, size);
				
				return createActionCatch(pair);
			}
			else //multi(fly)catch
			{
				int numIndices = indices.size();
				Action[] actions = new Action[numIndices-1];
				for(int i=0; i < numIndices - 1; i++)
				{
					int fromIndex = indices.get(i);
					int toIndex = indices.get(i+1);
					RestrictedLocationPair pair = new RestrictedLocationPair(fromIndex, toIndex, size);
					actions[i] = createActionCatch(pair); //possibly double composite: MultiFlyCatch = (([Steps] + Catch) + ([Steps] + Catch))
				}
				return new CompositeAction(actions);
			}
		}
	}

	private static Action createActionMove(RestrictedLocationPair pair) {
		if(pair.getDiagonalDistance() == 1)
		{
			return new AtomicActionStep(pair);
		}
		else //dist > 1
		{
			return new ActionFly(pair);
		}
	}

	private static Action createActionCatch(RestrictedLocationPair pair) {
		if(pair.getDiagonalDistance() == 2)
		{
			return new AtomicActionCatch(pair);
		}
		else if(pair.getDiagonalDistance() > 2)
		{
			return new ActionFlyCatch(pair);
		}
		else //dist == 1
		{
			throw new IllegalArgumentException("Pair too close to be a catch");
		}
	}
	
	private static AnalysisResult analyzeMove(String move)
	{
		
		if(move.matches("(\\d+)\\s*-\\s*(\\d+)")) //step or fly
		{
			Pattern movePattern = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)");
			Matcher matcher = movePattern.matcher(move);
			if(!matcher.matches())
			{
				assert(false);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			return new AnalysisResult(false, fromIndex, toIndex);
		}
		else if(move.matches("(\\d+)\\s*x\\s*(\\d+)"))
		{
			Pattern catchPattern = Pattern.compile("(\\d+)\\s*x\\s*(\\d+)");
			Matcher matcher = catchPattern.matcher(move);
			if(!matcher.matches())
			{
				assert(false);
			}
			int fromIndex = Integer.parseInt(matcher.group(1));
			int toIndex = Integer.parseInt(matcher.group(2));
			
			return new AnalysisResult(true, fromIndex, toIndex);
		}
		else if(move.matches("(\\d+)(\\s*x\\s*(\\d+))+")) //Multi(Fly)Catch
		{
			String[] parts = move.split("\\s*x\\s*");
			AnalysisResult result = new AnalysisResult(true);
			for(int i=0; i < parts.length; i++)
			{
				int index = Integer.parseInt(parts[i]);
				result.addIndex(index);
			}
			return result;
		}
		throw new IllegalArgumentException("Invalid pattern");
	}
	
	private static class AnalysisResult
	{
		private final boolean isCatch;
		private final List<Integer> indices = new ArrayList<Integer>();
		
		public AnalysisResult(boolean isCatch, int... indices) {
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
}
