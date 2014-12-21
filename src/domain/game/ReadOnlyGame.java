package domain.game;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class ReadOnlyGame implements IReadOnlyGame
{
	private final IGame game;
	
	private IGame getGame()
	{
		return game;
	}
	
	public ReadOnlyGame(IGame game)
	{
		this.game = game;
	}
	
	@Override
	public IReadOnlyBoard getBoard()
	{
		return getGame().getBoard().getReadOnlyBoard();
	}

	@Override
	public Player getCurrentPlayer()
	{
		return getGame().getCurrentPlayer();
	}

	@Override
	public boolean isOver()
	{
		return getGame().isOver();
	}

	@Override
	public Player getWinner()
	{
		return getGame().getWinner();
	}

	@Override
	public GameState getGameState()
	{
		return getGame().getGameState();
	}

	@Override
	public IReadOnlyGame getReadOnlyGame()
	{
		return this;
	}
}
