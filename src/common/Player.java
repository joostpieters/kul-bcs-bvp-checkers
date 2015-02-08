package common;

import domain.piece.Piece;
import ui.LocalizationManager;

/**
 * Represents a player participating in this game, identified by the color of his {@link Piece}s.
 */
public enum Player
{
	Black,
	White;

	/**
	 * Returns the opponent of this player.
	 */
	public Player getOpponent()
	{
		return this == Black ? White : Black;
	}
	
	@Override
	public String toString()
	{
		return this == Black
				? LocalizationManager.getString("playerBlack")
				: LocalizationManager.getString("playerWhite");
	}
}
