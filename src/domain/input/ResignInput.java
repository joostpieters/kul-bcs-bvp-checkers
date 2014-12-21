package domain.input;

import common.Player;
import domain.game.contracts.IReadOnlyGame;
import domain.input.contracts.IInput;
import domain.updates.UpdateSource;

public class ResignInput extends UpdateSource implements IInput
{
	private final IReadOnlyGame game;
	
	private IReadOnlyGame getGame()
	{
		return game;
	}
	
	public ResignInput(IReadOnlyGame game)
	{
		this.game = game;
	}
	
	@Override
	public boolean process()
	{
		Player resignee = getGame().getCurrentPlayer(); 
		emitResign(resignee);
		return true;
	}
}
