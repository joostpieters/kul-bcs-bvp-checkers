package domain.input;

import common.Player;
import domain.Game;
import domain.input.contracts.IInput;
import domain.updates.GameUpdateSource;

public class ResignInput extends GameUpdateSource implements IInput
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
		Player winner = currentPlayer.getOpponent();
		getGame().getUI().resign(currentPlayer);
		getGame().gameOver(winner);
		updateFollowersGameOver(winner);
		return true;
	}
}
