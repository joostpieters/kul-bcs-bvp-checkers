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
		Player player = game.getCurrentPlayer();
		UserInterface ui = game.getUI();
		
		if(ui.askRemise(player))
		{
			ui.agreeRemise();
			game.remise();
			updateFollowersGameOver(null);
			return true;
		}
		else
		{
			ui.disagreeRemise();
			return false;
		}
	}		
}
