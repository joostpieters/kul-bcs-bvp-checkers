package domain.action;

import java.util.List;

import common.Player;
import domain.action.request.ActionRequest;
import domain.board.BoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;

public class ActionFactory {
	private ActionFactory() { }
	
	public static Action create(ActionRequest request, IReadOnlyBoard board, Player currentPlayer) {
		BoardSize size = board.getSize();
		List<Integer> indices = request.getIndices();
		if(!request.isCatch())
		{
			assert(indices.size() == 2);
			int fromIndex = indices.get(0);
			int toIndex = indices.get(1);
			DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
			
			return createActionMove(pair);
		}
		else
		{
			if(indices.size() == 2)
			{
				int fromIndex = indices.get(0);
				int toIndex = indices.get(1);
				DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
				
				return createActionCatch(board, currentPlayer, pair);
			}
			else //multi(fly)catch
			{
				int numIndices = indices.size();
				Action[] actions = new Action[numIndices-1];
				for(int i=0; i < numIndices - 1; i++)
				{
					int fromIndex = indices.get(i);
					int toIndex = indices.get(i+1);
					DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
					actions[i] = createActionCatch(board, currentPlayer, pair);
				}
				return new CompositeAction(actions);
			}
		}
	}

	private static Action createActionMove(DiagonalLocationPair pair) {
		if(pair.getDiagonalDistance() == 1)
		{
			return new AtomicActionStep(pair);
		}
		else //dist > 1
		{
			return new CompositeActionFly(pair);
		}
	}

	private static Action createActionCatch(IReadOnlyBoard board, Player currentPlayer, DiagonalLocationPair pair) {
		if(pair.getDiagonalDistance() == 2)
		{
			return new AtomicActionCatch(pair);
		}
		else if(pair.getDiagonalDistance() > 2)
		{
			return new CompositeActionFlyCatch(board, currentPlayer, pair);
		}
		else //dist == 1
		{
			throw new IllegalArgumentException("Pair too close to be a catch");
		}
	}
}
