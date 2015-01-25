package domain.input;

import static org.junit.Assert.*;

import org.junit.Test;

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

public class ResignInputTest implements IObserver
{
	private boolean hasResigned = false;
	
	@Test
	public void testProcess()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		ResignInput input = new ResignInput(game);
		input.subscribe(this);
		boolean result = input.process();
		assertTrue(result);
		assertTrue(hasResigned);
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
	}

	@Override
	public void acceptRemise()
	{
	}

	@Override
	public void declineRemise()
	{
	}

	@Override
	public void forcedRemise()
	{
	}

	@Override
	public void resign(Player resignee)
	{
		hasResigned = true;
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
