package ui;

import ui.contracts.IVisualizer;
import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

public class TextualVisualizer implements IVisualizer
{
	@Override
	public void paint(IReadOnlyBoard board)
	{
		System.out.println(board);
	}

	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
		paint(board);
	}

	@Override
	public void gameOver(Player winner)
	{
		System.out.printf(LocalizationManager.getString("gameOver"), winner);
	}

	@Override
	public void promotion(IReadOnlyBoard board, Location location)
	{
		System.out.println(LocalizationManager.getString("promotion"));
		paint(board);
	}

	@Override
	public void outOfMoves(Player player)
	{
		System.out.printf(LocalizationManager.getString("outOfMoves"), player);
	}

	@Override
	public void proposeRemise(Player proposer)
	{
		System.out.printf(LocalizationManager.getString("proposeRemise"), proposer);
	}

	@Override
	public void acceptRemise()
	{
		System.out.println(LocalizationManager.getString("acceptRemise"));
	}

	@Override
	public void declineRemise()
	{
		System.out.println(LocalizationManager.getString("declineRemise"));
	}

	@Override
	public void resign(Player resignee)
	{
		System.out.printf(LocalizationManager.getString("resign"), resignee);
	}

	@Override
	public void start(IReadOnlyBoard board, Player starter)
	{
		System.out.printf(LocalizationManager.getString("gameStart"), starter);
		paint(board);
	}

	@Override
	public void warning(String message)
	{
		System.out.printf(LocalizationManager.getString("warning"), message);
	}

	@Override
	public void error(String message, Exception ex)
	{
		System.out.printf(LocalizationManager.getString("error"), message);
		ex.printStackTrace();
	}

	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		System.out.printf(LocalizationManager.getString("switchPlayer"), switchedIn);
	}

	@Override
	public void executeAction(IAction action)
	{
		System.out.println(action);
	}

	@Override
	public void forcedRemise()
	{
		System.out.println(LocalizationManager.getString("forcedRemise"));
	}
}
