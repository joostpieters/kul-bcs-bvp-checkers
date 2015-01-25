package domain.input;

import static org.junit.Assert.*;

import org.junit.Test;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ui.contracts.IUserInterface;
import common.Player;
import domain.action.contracts.IAction;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.Game;
import domain.game.contracts.IGame;
import domain.location.Location;
import domain.updates.contracts.IObserver;

public class RemiseInputTest implements IObserver
{
	private boolean isRemiseProposed = false;
	private boolean isRemiseAccepted = false;
	
	private IUserInterface getYesNoUI(boolean yesno)
	{
		return new IUserInterface()
		{
			@Override
			public void close()
			{
				throw new NotImplementedException();
			}
			
			@Override
			public boolean askYesNo(String message)
			{
				return yesno;
			}
			
			@Override
			public String askInput(String message)
			{
				throw new NotImplementedException();
			}
			
			@Override
			public String askActionInput(Player player)
			{
				throw new NotImplementedException();
			}
		};
	}
	
	@Test
	public void testProcessAcceptRemise()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		RemiseInput input = new RemiseInput(game, getYesNoUI(true));
		input.subscribe(this);
		boolean result = input.process();
		assertTrue(result);
		assertTrue(isRemiseProposed);
		assertTrue(isRemiseAccepted);
	}
	
	@Test
	public void testProcessDeclineRemise()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		RemiseInput input = new RemiseInput(game, getYesNoUI(false));
		input.subscribe(this);
		boolean result = input.process();
		assertFalse(result);
		assertTrue(isRemiseProposed);
		assertFalse(isRemiseAccepted);
	}

	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
	}

	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
	}

	@Override
	public void executeAction(IAction action)
	{
	}

	@Override
	public void promotion(IReadOnlyBoard board, Location location)
	{
	}

	@Override
	public void gameOver(Player winner)
	{
	}

	@Override
	public void outOfMoves(Player player)
	{
	}

	@Override
	public void proposeRemise(Player proposer)
	{
		isRemiseProposed = true;
	}

	@Override
	public void acceptRemise()
	{
		isRemiseAccepted = true;
	}

	@Override
	public void declineRemise()
	{
		isRemiseAccepted = false;
	}

	@Override
	public void forcedRemise()
	{
	}

	@Override
	public void resign(Player resignee)
	{
	}

	@Override
	public void start(IReadOnlyBoard board, Player starter)
	{
	}

	@Override
	public void warning(String message)
	{
	}

	@Override
	public void error(String message, Exception ex)
	{
	}
}
