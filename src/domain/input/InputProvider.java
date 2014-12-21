package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.LegalActionChecker;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;
import domain.updates.UpdatePropagator;


public class InputProvider extends UpdatePropagator implements AutoCloseable
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
	
	@Override
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
	
	public IInput askInput(IGame game)
	{
		if(isClosed())
		{
			throw new IllegalStateException(LocalizationManager.getString("closedProviderException"));
		}
		Player player = game.getCurrentPlayer();
		String move = getUI().askActionInput(player);
		if(move.equals(LocalizationManager.getString("resignInput")))
		{
			ResignInput input = new ResignInput(game.getReadOnlyGame());
			input.subscribe(this);
			return input;
		}
		else if(move.equals(LocalizationManager.getString("remiseInput")))
		{
			RemiseInput input = new RemiseInput(game.getReadOnlyGame(), getUI());
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
