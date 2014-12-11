package domain.input;

import common.Player;
import domain.Game;

public class ResignInput implements IInput
{
	private final Game game;
	
	private Game getGame()
	{
		return game;
	}
	
	public ResignInput(Game game)
	{
		this.game = game;
	}
	
	@Override
	public boolean process()
	{
		Player currentPlayer = getGame().getCurrentPlayer(); 
		getGame().getUI().resign(currentPlayer);
		getGame().gameOver(currentPlayer.getOpponent());
		return true;
	}
}
