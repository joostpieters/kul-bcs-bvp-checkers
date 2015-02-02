package ui.contracts;

import common.Player;

/**
 * A basic interface for interacting with the player.
 */
public interface IUserInterface
{
	/**
	 * Ask the player a yes/no question.
	 * @param 	message
	 * 			The question to display.
	 * @return	true and false for yes and no respectively.
	 */
	public boolean askYesNo(String message);

	/**
	 * Ask the player for textual input.
	 * @param 	message
	 * 			The message to display.
	 * @return	The text entered by the player.
	 */
	public String askInput(String message);

	/**
	 * Ask the player for action input.
	 * @param 	player
	 * 			The {@link Player} whose input is needed.
	 * @return	The text entered by the player.
	 */
	public String askActionInput(Player player);
}