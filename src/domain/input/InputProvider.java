package domain.input;

import common.Configs;
import common.Player;
import domain.Game;
import domain.input.contracts.IInput;
import domain.updates.GameUpdatePropagator;


public class InputProvider extends GameUpdatePropagator
{
	public IInput askInput(Game game)
	{
		Player player = game.getCurrentPlayer();
		String move = game.getUI().askMoveInput(player);
		if(move.equals(Configs.ResignInput))
		{
			ResignInput input = new ResignInput(game);
			input.subscribe(this);
			return input;
		}
		else if(move.equals(Configs.RemiseInput))
		{
			RemiseInput input = new RemiseInput(game);
			input.subscribe(this);
			return input;
		}
		else
		{
			ActionInput input = new ActionInput(move, game);
			input.subscribe(this);
			return input;
		}
	}
}
