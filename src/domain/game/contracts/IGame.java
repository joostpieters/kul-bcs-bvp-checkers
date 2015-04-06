package domain.game.contracts;

import common.Player;
import domain.board.contracts.IBoard;

/**
 * Represents a game with a corresponding {@link IBoard}, current {@link Player} and {@link GameState}.
 */
public interface IGame extends IReadOnlyGame
{
	/**
	 * Switch the current {@link Player} to his opponents.
	 */
	public void switchCurrentPlayer();

	/**
	 * Sets the {@link GameState} to {@link GameState#Remise}.
	 */
	public void remise();

	/**
	 * Sets the {@link GameState} to {@link GameState#Finished} and saves the winner.
	 * @param 	winner
	 * 			The {@link Player} who won the {@link IGame}.
	 */
	public void gameOver(Player winner);
	
	/**
	 * Returns the {@link IBoard} this {@link IGame} is played on.
	 */
	@Override
	public IBoard getBoard();
}