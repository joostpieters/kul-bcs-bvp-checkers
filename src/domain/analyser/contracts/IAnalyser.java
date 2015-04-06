package domain.analyser.contracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;

public interface IAnalyser<T extends IActionRequest>
{
	/**
	 * Returns the {@link IReadOnlyBoard} this {@link IAnalyser} uses.
	 */
	IReadOnlyBoard getBoard();
	
	/**
	 * Finds {@link IActionRequest}s the given {@link Player} can choose from,
	 * using any of his {@link IPiece}s.
	 */
	default List<T> find(Player currentPlayer)
	{
		List<T> result = new ArrayList<T>();
		IReadOnlyBoard board = getBoard();
		HashMap<Location,IPiece> pieceLocations = board.getPlayerPieces(currentPlayer);
		for(Location pieceLocation : pieceLocations.keySet())
		{
			List<T> catchActionsForPiece = find(currentPlayer, pieceLocation);
			result.addAll(catchActionsForPiece);
		}
		
		return result;
	}
	
	/**
	 * Finds {@link IActionRequest}s the given {@link Player} can choose from, 
	 * using the {@link IPiece} at the given {@link Location}. 
	 */
	List<T> find(Player currentPlayer, Location pieceLocation);
}
