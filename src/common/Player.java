package common;

import ui.LocalizationManager;

public enum Player
{
	Black,
	White;

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
