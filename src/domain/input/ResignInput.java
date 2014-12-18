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
		Player resignee = getGame().getCurrentPlayer(); 
		updateObserversResign(resignee);
		return true;
	}
}
