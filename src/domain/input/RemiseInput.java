package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.Game;
import domain.input.contracts.IInput;
import domain.updates.GameUpdateSource;

public class RemiseInput extends GameUpdateSource implements IInput
{
	private final Game game;
	private final IUserInterface ui;
	
	private Game getGame()
	{
		return game;
	}
	
	private IUserInterface getUI()
	{
		return ui;
	}
	
	public RemiseInput(Game game, IUserInterface ui)
	{
		this.game = game;
		this.ui = ui;
	}
	
	@Override
	public boolean process()
	{
		Game game = getGame();
		Player proposer = game.getCurrentPlayer();
		
		updateObserversProposeRemise(proposer);
		
		if(getUI().askYesNo(LocalizationManager.getString("acceptRemise")))
		{
			updateObserversAcceptRemise();
			return true;
		}
		else
		{
			updateObserversDeclineRemise();
			return false;
		}
	}		
}
