package domain.input;

import ui.UserInterface;
import common.Configs;
import common.Player;
import domain.Game;


public class InputProvider
{
	private final UserInterface ui;
	
	private UserInterface getUI() {
		return ui;
	}
	
	public InputProvider(UserInterface ui)
	{
		this.ui = ui;
	}
	
	public IInput askInput(Game game)
	{
		Player player = game.getCurrentPlayer();
		String move = getUI().askMoveInput(player);
		if(move.equals(Configs.ResignInput))
		{
			return new ResignInput(game);
		}
		else if(move.equals(Configs.RemiseInput))
		{
			return new RemiseInput(game);
		}
		else
		{
			return new ActionInput(move, game);
		}
	}
}
