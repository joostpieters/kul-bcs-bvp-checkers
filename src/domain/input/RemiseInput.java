package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.game.contracts.IReadOnlyGame;
import domain.input.contracts.IInput;
import domain.update.UpdateSource;

/**
 * A kind of {@link IInput} that represents the intent of the player to propose remise.
 */
public class RemiseInput extends UpdateSource implements IInput
{
	private final IReadOnlyGame game;
	private final IUserInterface ui;
	
	private IReadOnlyGame getGame()
	{
		return game;
	}
	
	private IUserInterface getUI()
	{
		return ui;
	}
	
	public RemiseInput(IReadOnlyGame game, IUserInterface ui)
	{
		this.game = game;
		this.ui = ui;
	}
	
	/**
	 * Proposes the remise to the other player and - if accepted - actually ends the game in remise. 
	 */
	@Override
	public boolean process()
	{
		IReadOnlyGame game = getGame();
		Player proposer = game.getCurrentPlayer();
		
		emitProposeRemise(proposer);
		
		if(getUI().askYesNo(LocalizationManager.getString("suggestRemise")))
		{
			emitAcceptRemise();
			return true;
		}
		else
		{
			emitDeclineRemise();
			return false;
		}
	}		
}
