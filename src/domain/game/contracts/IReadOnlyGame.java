package domain.game.contracts;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;

/**
 * Represents a read-only version of an {@link IGame}.
 */
public interface IReadOnlyGame
{
	/**
	 * Returns the read-only {@link IBoard} of this {@link IGame}.
	 */
	public IReadOnlyBoard getBoard();

	/**
	 * Returns the current {@link Player}.
	 */
	public Player getCurrentPlayer();

	/**
	 * Returns false if the {@link GameState} is Ongoing, true otherwise.
	 */
	public boolean isOver();

	/**
	 * Returns the winning {@link Player} if available.
	 */
	public Player getWinner();

	/**
	 * Returns the {@link GameState} corresponding to this {@link IGame}.
	 */
	public GameState getGameState();

	/**
	 * Returns a read-only version of this {@link IGame}.
	 */
	public IReadOnlyGame getReadOnlyGame();
}