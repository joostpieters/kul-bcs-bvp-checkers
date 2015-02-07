package domain.action.contracts;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.update.contracts.IBasicUpdateSource;

/**
 * Basic interface for all possible actions a {@link Player} can perform with his {@link IPiece}s.
 */
public interface IAction extends IBasicUpdateSource
{	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * @Note 	This method only checks the {@link IAction} in its own limited scope.
	 * 			Game rules about priority moves etc. are ignored here.
	 */
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer);

	/**
	 * Executes this {@link IAction} on the given {@link IBoard} for the given {@link Player}. 
	 * Execution cannot proceed if this {@link IAction} is not valid on the given {@link IBoard}.
	 */
	public void executeOn(IBoard board, Player currentPlayer);

	/**
	 * Returns the start {@link Location}.
	 */
	public Location getFrom();
	
	/**
	 * Returns true if this {@link IAction} performs a catch,
	 * false otherwise.
	 */
	public boolean isCatch();
}
