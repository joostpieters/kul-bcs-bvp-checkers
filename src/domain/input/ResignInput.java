package domain.input;

import common.Player;
import domain.game.contracts.IReadOnlyGame;
import domain.input.contracts.IInput;
import domain.update.UpdateSource;

/**
 * A kind of {@link IInput} that represents the player's intent to resign.
 *
 */
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

	/**
	 * Makes the current player resign.
	 */
	@Override
	public boolean process()
	{
		Player resignee = getGame().getCurrentPlayer(); 
		emitResign(resignee);
		return true;
	}
}
