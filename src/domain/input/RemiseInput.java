package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.game.contracts.IReadOnlyGame;
import domain.input.contracts.IInput;
import domain.updates.UpdateSource;

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
	
	@Override
	public boolean process()
	{
		IReadOnlyGame game = getGame();
		Player proposer = game.getCurrentPlayer();
		
		emitProposeRemise(proposer);
		
		if(getUI().askYesNo(LocalizationManager.getString("acceptRemise")))
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
