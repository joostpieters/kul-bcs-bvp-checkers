package domain.input;

import ui.UserInterface;
import common.Player;
import domain.Game;
import domain.input.contracts.IInput;
import domain.updates.GameUpdateSource;

public class RemiseInput extends GameUpdateSource implements IInput
{
	private final Game game;
	
	private Game getGame()
	{
		return game;
	}
	
	public RemiseInput(Game game)
	{
		this.game = game;
	}
	
	@Override
	public boolean process()
	{
		Game game = getGame();
		Player proposer = game.getCurrentPlayer();
		UserInterface ui = game.getUI();
		
		updateObserversProposeRemise(proposer);
		
		if(ui.askYesNo("Accept remise?"))
		{
			updateObserversAgreeRemise();
			return true;
		}
		else
		{
			updateObserversDisagreeRemise();
			return false;
		}
	}		
}
