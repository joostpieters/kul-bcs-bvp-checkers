package domain.input;

import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.action.LegalActionChecker;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;
import domain.input.contracts.IInputProvider;
import domain.update.UpdatePropagator;


public class InputProvider extends UpdatePropagator implements AutoCloseable, IInputProvider
{
	private final IUserInterface ui;
	private final LegalActionChecker legalActionChecker;
	private boolean closed = false;
	private final IGame game;
	
	private IUserInterface getUI()
	{
		return ui;
	}
	
	private LegalActionChecker getLegalActionChecker()
	{
		return legalActionChecker;
	}
	
	private IGame getGame()
	{
		return game;
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
	
	public InputProvider(IUserInterface ui, LegalActionChecker legalActionChecker, IGame game)
	{
		this.ui = ui;
		this.legalActionChecker = legalActionChecker;
		this.game = game;
	}
	
	@Override
	public IInput askInput()
	{
		if(isClosed())
		{
			throw new IllegalStateException(LocalizationManager.getString("closedProviderException"));
		}
		Player player = getGame().getCurrentPlayer();
		String move = getUI().askActionInput(player);
		if(move.equals(LocalizationManager.getString("resignInput")))
		{
			ResignInput input = new ResignInput(getGame().getReadOnlyGame());
			input.subscribe(this);
			return input;
		}
		else if(move.equals(LocalizationManager.getString("remiseInput")))
		{
			RemiseInput input = new RemiseInput(getGame().getReadOnlyGame(), getUI());
			input.subscribe(this);
			return input;
		}
		else
		{
			ActionInput input = new ActionInput(move, getGame(), getLegalActionChecker());
			input.subscribe(this);
			return input;
		}
	}
}
