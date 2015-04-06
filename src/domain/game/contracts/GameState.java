package domain.game.contracts;

import common.Player;

import domain.game.Game;

/**
 * An enumeration of possible {@link Game} states.
 */
public enum GameState
{
	/**
	 * The {@link Game} is ongoing.
	 */
	Ongoing,
	
	/**
	 * Both {@link Player}s agreed to remise.
	 */
	Remise,
	
	/**
	 * The {@link Game} finished with a clear winner.
	 */
	Finished
}
