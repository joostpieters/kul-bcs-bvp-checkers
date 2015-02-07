package domain.action;

import java.util.List;
import java.util.Set;

import ui.LocalizationManager;
import common.Player;
import domain.action.contracts.IAction;
import domain.action.contracts.IActionRequest;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;

/**
 * This class contains a factory method to handle the complex process of creating {@link IAction}s.
 */
public class ActionFactory
{
	private ActionFactory() { }
	
	/**
	 * Factory method to create {@link IAction}s based on a given {@link IActionRequest}.
	 * 
	 * @param 	request
	 * 			The {@link IActionRequest} to create the new {@link IAction} from.
	 * @param 	board
	 * 			The {@link IBoard} of the current {@link IGame}.
	 * @param 	currentPlayer
	 * 			The {@link Player} that requests the {@link IAction}.
	 * @throws 	LocationOutOfRangeException
	 * 			When the {@link IActionRequest} contains invalid indices.
	 */
	public static IAction create(IActionRequest request, IReadOnlyBoard board, Player currentPlayer) throws LocationOutOfRangeException
	{
		IBoardSize size = board.getSize();
		List<Integer> indices = request.getIndices();
		if(!request.isCatch())
		{
			return createActionMove(size, indices);
		}
		else if(indices.size() == 2)
		{
			Set<Location> opponentPieceLocations = board.getPlayerPieces(currentPlayer.getOpponent()).keySet();
			return createSingleActionCatch(opponentPieceLocations, currentPlayer, size, indices);
		}
		else
		{
			return createMultiActionCatch(board, currentPlayer, size, indices);
		}
	}

	private static IAction createMultiActionCatch(IReadOnlyBoard board, Player currentPlayer, IBoardSize size, List<Integer> indices) throws LocationOutOfRangeException
	{
		int numIndices = indices.size();
		IAction[] actions = new IAction[numIndices-1];
		IBoard testBoard = board.getDeepClone(); //need up-to-date board for every step to locate opponent pieces
		for(int i=0; i < numIndices - 1; i++)
		{
			int fromIndex = indices.get(i);
			int toIndex = indices.get(i+1);
			DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
			actions[i] = createActionCatch(testBoard.getPlayerPieces(currentPlayer.getOpponent()).keySet(), currentPlayer, pair);
			if(actions[i].isValidOn(testBoard, currentPlayer))
			{
				actions[i].executeOn(testBoard, currentPlayer);
			}
			else
			{
				throw new IllegalStateException("Could not create multi(fly)catch action: invalid subactions.");
			}
		}
		return new CompositeAction(actions);
	}

	private static IAction createSingleActionCatch(Set<Location> opponentPieceLocations, Player currentPlayer, IBoardSize size, List<Integer> indices) throws LocationOutOfRangeException
	{
		int fromIndex = indices.get(0);
		int toIndex = indices.get(1);
		DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
		
		return createActionCatch(opponentPieceLocations, currentPlayer, pair);
	}

	private static IAction createActionMove(IBoardSize size, List<Integer> indices) throws LocationOutOfRangeException
	{
		assert(indices.size() == 2); //MoveActionRequest always has two indices
		int fromIndex = indices.get(0);
		int toIndex = indices.get(1);
		DiagonalLocationPair pair = new DiagonalLocationPair(fromIndex, toIndex, size);
		
		return createActionMove(pair);
	}

	private static IAction createActionMove(DiagonalLocationPair pair)
	{
		if(pair.getDiagonalDistance() == 1)
		{
			return new AtomicActionStep(pair);
		}
		else
		{
			return new CompositeActionFly(pair);
		}
	}

	private static IAction createActionCatch(Set<Location> opponentPieceLocations, Player currentPlayer, DiagonalLocationPair pair)
	{
		if(pair.getDiagonalDistance() == 2)
		{
			return new AtomicActionCatch(pair);
		}
		else if(pair.getDiagonalDistance() > 2)
		{
			return new CompositeActionFlyCatch(opponentPieceLocations, currentPlayer, pair);
		}
		else //dist == 1
		{
			throw new IllegalArgumentException(LocalizationManager.getString("errorPairTooClose"));
		}
	}
}
