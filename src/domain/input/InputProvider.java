package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Configs;
import common.Player;
import domain.Game;
import domain.LegalActionChecker;
import domain.input.contracts.IInput;
import domain.updates.GameUpdatePropagator;


public class InputProvider extends GameUpdatePropagator
{
	private final IUserInterface ui;
	private final LegalActionChecker legalActionChecker;
	private boolean closed = false;
	
	private IUserInterface getUI()
	{
		return ui;
	}
	
	private LegalActionChecker getLegalActionChecker()
	{
		return legalActionChecker;
	}
	
	private boolean isClosed()
	{
		return closed;
	}
	
	public void close()
	{
		this.closed = true;
		getUI().close();
	}
	
	public InputProvider(IUserInterface ui, LegalActionChecker legalActionChecker)
	{
		this.ui = ui;
		this.legalActionChecker = legalActionChecker;
	}
	
	public IInput askInput(Game game)
	{
		if(isClosed())
		{
			throw new IllegalStateException(LocalizationManager.getString("closedProviderException"));
		}
		Player player = game.getCurrentPlayer();
		String move = getUI().askActionInput(player);
		if(move.equals(Configs.ResignInput))
		{
			ResignInput input = new ResignInput(game);
			input.subscribe(this);
			return input;
		}
		else if(move.equals(Configs.RemiseInput))
		{
			RemiseInput input = new RemiseInput(game, getUI());
			input.subscribe(this);
			return input;
		}
		else
		{
			ActionInput input = new ActionInput(move, game, getLegalActionChecker());
			input.subscribe(this);
			return input;
		}
	}
}
