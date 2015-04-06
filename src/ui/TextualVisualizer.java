package ui;

import ui.contracts.IVisualizer;
import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.location.Location;

/**
 * Visualizes the {@link IGame}'s progress through text output.
 */
public class TextualVisualizer implements IVisualizer
{
	@Override
	public void paint(IReadOnlyBoard board)
	{
		System.out.println(board);
	}

	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		paint(board);
	}

	@Override
	public void fireGameOver(Player winner)
	{
		System.out.printf(LocalizationManager.getString("gameOver"), winner);
	}

	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
		System.out.println(LocalizationManager.getString("promotion"));
		paint(board);
	}

	@Override
	public void fireOutOfMoves(Player player)
	{
		System.out.printf(LocalizationManager.getString("outOfMoves"), player);
	}

	@Override
	public void fireProposeRemise(Player proposer)
	{
		System.out.printf(LocalizationManager.getString("proposeRemise"), proposer);
	}

	@Override
	public void fireAcceptRemise()
	{
		System.out.println(LocalizationManager.getString("acceptRemise"));
	}

	@Override
	public void fireDeclineRemise()
	{
		System.out.println(LocalizationManager.getString("declineRemise"));
	}

	@Override
	public void fireResign(Player resignee)
	{
		System.out.printf(LocalizationManager.getString("resign"), resignee);
	}

	@Override
	public void fireStart(IReadOnlyBoard board, Player starter)
	{
		System.out.printf(LocalizationManager.getString("gameStart"), starter);
		paint(board);
	}

	@Override
	public void fireWarning(String message)
	{
		System.out.printf(LocalizationManager.getString("warning"), message);
	}

	@Override
	public void fireError(String message, Exception ex)
	{
		System.out.printf(LocalizationManager.getString("error"), message);
		ex.printStackTrace();
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		System.out.printf(LocalizationManager.getString("switchPlayer"), switchedIn);
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
		System.out.println(action);
	}

	@Override
	public void fireForcedRemise()
	{
		System.out.println(LocalizationManager.getString("forcedRemise"));
	}
}
